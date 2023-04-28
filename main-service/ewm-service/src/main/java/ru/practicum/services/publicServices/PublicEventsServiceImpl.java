package ru.practicum.services.publicServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Client;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.mapper.EventMapper;
import ru.practicum.dto.request.RequestParamPublicForEvent;
import ru.practicum.enums.State;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.EventSearchCriteria;
import ru.practicum.repository.EventRepository;
import ru.practicum.utils.MyPageRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicEventsServiceImpl implements PublicEventsService {

    private final EventRepository eventRepository;

    private final Client statsClient;

    @Value("${main.service.name}")
    private String serviceName;

    @Transactional
    @Override
    public Set<EventShortDto> getAll(RequestParamPublicForEvent param) {
        MyPageRequest pageable = createPageable(param.getSort(), param.getFrom(), param.getSize());
        EventSearchCriteria eventSearchCriteria = createCriteria(param);

        Set<EventShortDto> eventShorts = EventMapper.toEventShortDtoList(eventRepository
                .findAllWithFilters(pageable, eventSearchCriteria).toSet());

        log.info("Get events list size: {}", eventShorts.size());
        saveEndpointHit(param.getRequest());
        return eventShorts;
    }


    @Transactional
    @Override
    public EventFullDto get(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event not found with id = %s", id)));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Event with id=%d is not published", id));
        }

        saveEndpointHit(request);
        log.info("Get event: {}", event.getId());
        event.setViews(event.getViews() + 1);
        eventRepository.flush();
        return EventMapper.toEventFullDto(event);
    }

    private void saveEndpointHit(HttpServletRequest request) {

        EndpointHitDto endpointHit = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(serviceName)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.post(endpointHit);
    }

    private MyPageRequest createPageable(String sort, int from, int size) {
        MyPageRequest pageable = null;
        if (sort == null || sort.equalsIgnoreCase("EVENT_DATE")) {
            pageable = new MyPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "event_date"));
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            pageable = new MyPageRequest(from, size,
                    Sort.by(Sort.Direction.ASC, "views"));
        }
        return pageable;
    }

    private EventSearchCriteria createCriteria(RequestParamPublicForEvent param) {
        return EventSearchCriteria.builder()
                .text(param.getText())
                .categories(param.getCategories())
                .rangeEnd(param.getRangeEnd())
                .rangeStart(param.getRangeStart())
                .paid(param.getPaid())
                .build();
    }
}