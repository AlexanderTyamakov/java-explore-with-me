package ru.practicum.services.privareServices;


import ru.practicum.dto.comments.CommentRequestDto;
import ru.practicum.dto.comments.CommentResponseDto;

public interface PrivateCommentsService {

    CommentResponseDto addComment(Long userId, Long eventId, CommentRequestDto commentDto);

    CommentResponseDto updateComment(Long userId, Long eventId, Long comId, CommentRequestDto commentDto);

    void deleteComment(Long userId, Long eventId, Long comId);

}
