package ru.practicum.services.privareServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comments.CommentRequestDto;
import ru.practicum.dto.comments.CommentResponseDto;
import ru.practicum.dto.event.*;
import ru.practicum.dto.mapper.CommentMapper;
import ru.practicum.dto.mapper.EventMapper;
import ru.practicum.dto.mapper.RequestMapper;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.enums.UserStateAction;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.*;
import ru.practicum.utils.Pagination;
import ru.practicum.utils.UtilMergeProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.enums.Status.CONFIRMED;
import static ru.practicum.enums.Status.REJECTED;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrivateEventsServiceImpl implements PrivateEventsService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    private final CategoriesRepository categoriesRepository;

    private final CommentRepository commentRepository;

    @Override
    public Set<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        Pagination pageRequest = new Pagination(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        Set<EventShortDto> eventShorts = EventMapper.toEventShortDtoList(eventRepository.findAll(pageRequest).toSet());
        log.info("Get events list size: {}", eventShorts.size());
        return eventShorts;
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        log.info("Get event: {}", event.getId());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException(
                    String.format("Event not found with id = %s and userId = %s", eventId, userId));
        }
        return RequestMapper.toDtoList(requestRepository.findAllByEventId(eventId));
    }

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        checkEventDate(eventDto.getEventDate());
        Event event = EventMapper.toEntity(eventDto);
        event.setCategory(categoriesRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found",
                        eventDto.getCategory()))));
        // event.setConfirmedRequests(0L);
        event.setPublishedOn(LocalDateTime.now());
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId))));
        event.setViews(0L);
        try {
            event = eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add event: {}", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequestDtoDto eventDto) {
        Event eventTarget = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        Event eventUpdate = EventMapper.toEntity(eventDto);
        checkEventDate(eventUpdate.getDate());

        if (eventDto.getCategory() != null) {
            eventUpdate.setCategory(categoriesRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found",
                            eventDto.getCategory()))));
        }

        if (eventTarget.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Event must not be published");
        }

        UtilMergeProperty.copyProperties(eventUpdate, eventTarget);
        if (UserStateAction.CANCEL_REVIEW.toString().equals(eventDto.getStateAction().toString())) {
            eventTarget.setState(State.CANCELED);
        } else if (UserStateAction.SEND_TO_REVIEW.toString().equals(eventDto.getStateAction().toString())) {
            eventTarget.setState(State.PENDING);
        }

        eventRepository.flush();
        log.info("Update event: {}", eventTarget.getTitle());
        return EventMapper.toEventFullDto(eventTarget);
    }


    @Transactional
    @Override
    public EventRequestStatusUpdateResultDto updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequestDto request) {
        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();

        List<Long> requestIds = request.getRequestIds();
        List<Request> requests = requestRepository.findAllByIdIn(requestIds);

        Status status = request.getStatus();

        if (status.equals(REJECTED)) {
            boolean isConfirmedRequestExists = requests.stream()
                    .anyMatch(r -> r.getStatus().equals(CONFIRMED));
            if (isConfirmedRequestExists) {
                throw new ConflictException("Cannot reject confirmed requests");
            }
            rejectedRequests = requests.stream()
                    .peek(r -> r.setStatus(REJECTED))
                    .map(RequestMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());
            return new EventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event not found with id = %s and userId = %s", eventId, userId)));
        Long participantLimit = event.getParticipantLimit();
        Long approvedRequests = event.getConfirmedRequests();
        long availableParticipants = participantLimit - approvedRequests;
        long potentialParticipants = requestIds.size();

        if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
            throw new ConflictException(String.format("Event with id=%d has reached participant limit", eventId));
        }

        if (status.equals(CONFIRMED)) {
            if (participantLimit.equals(0L) || (potentialParticipants <= availableParticipants && !event.getRequestModeration())) {
                confirmedRequests = requests.stream()
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been confirmed", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been confirmed", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(REJECTED)) {
                                r.setStatus(REJECTED);
                            } else {
                                throw new ConflictException(String.format("Request with id=%d has already been rejected", r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(participantLimit);
            }
        }
        eventRepository.flush();
        requestRepository.flush();
        return new EventRequestStatusUpdateResultDto(confirmedRequests, rejectedRequests);
    }

    @Override
    @Transactional
    public CommentResponseDto addComment(Long userId, Long eventId, CommentRequestDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id = %s", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event not found with id = %s", eventId)));
        Comment comment = CommentMapper.toComment(commentDto, user, event);
        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(Long userId, Long eventId, Long comId, CommentRequestDto commentDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id = %s", userId)));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment not found with id = %s", comId)));
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new NotFoundException(String.format("Event with id=%d does not have comment with id=%d", eventId, comId));
        }
        comment.setText(commentDto.getText());
        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long eventId, Long comId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id = %s", userId)));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment not found with id = %s", comId)));
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new NotFoundException(String.format("Event with id=%d does not have comment with id=%d", eventId, comId));
        }
        commentRepository.deleteById(comId);
    }


    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Field: eventDate. Error: the date and time for which the event is scheduled" +
                    " cannot be earlier than two hours from the current moment. Value: " + eventDate);
        }
    }

}
