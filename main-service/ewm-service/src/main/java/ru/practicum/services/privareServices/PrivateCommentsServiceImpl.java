package ru.practicum.services.privareServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comments.CommentRequestDto;
import ru.practicum.dto.comments.CommentResponseDto;
import ru.practicum.dto.mapper.CommentMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PrivateCommentsServiceImpl implements PrivateCommentsService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;


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
}
