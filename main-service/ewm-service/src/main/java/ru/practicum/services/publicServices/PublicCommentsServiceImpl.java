package ru.practicum.services.publicServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comments.CommentResponseDto;
import ru.practicum.dto.mapper.CommentMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicCommentsServiceImpl implements PublicCommentsService {

    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;


    @Transactional
    @Override
    public List<CommentResponseDto> getAllComments(Long id, Integer from, Integer size) {
        eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event not found with id = %s", id)));
        List<Comment> comments = commentRepository.findAllByEventId(id, PageRequest.of(from, size));
        log.info("Get comments list size: {}", comments.size());
        return CommentMapper.toCommentResponseDto(comments);
    }

    @Transactional
    @Override
    public CommentResponseDto getComment(Long eventId, Long comId) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment not found with id = %s", comId)));
        if (!Objects.equals(comment.getEvent().getId(), eventId)) {
            throw new NotFoundException(String.format("Event with id=%d does not have comment with id=%d", eventId, comId));
        }
        log.info("Get comment: {}", eventId);
        return CommentMapper.toCommentResponseDto(comment);
    }
}