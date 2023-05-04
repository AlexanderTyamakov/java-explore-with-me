package ru.practicum.services.publicServices;

import ru.practicum.dto.comments.CommentResponseDto;

import java.util.List;

public interface PublicCommentsService {

    CommentResponseDto getComment(Long id, Long comId);

    List<CommentResponseDto> getAllComments(Long id, Integer from, Integer size);
}
