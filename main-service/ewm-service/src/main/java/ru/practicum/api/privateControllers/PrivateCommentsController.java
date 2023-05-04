package ru.practicum.api.privateControllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comments.CommentRequestDto;
import ru.practicum.dto.comments.CommentResponseDto;
import ru.practicum.services.privareServices.PrivateCommentsService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comments")
public class PrivateCommentsController {

    public final PrivateCommentsService service;

    @PostMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody @Valid CommentRequestDto comment) {
        log.info("Получен запрос POST /comments/users/{}/events/{} c новым комментарием: {}", userId, eventId, comment);
        return new ResponseEntity<>(service.addComment(userId, eventId, comment), HttpStatus.CREATED);
    }

    @PatchMapping("{comId}/users/{userId}/events/{eventId}")
    public ResponseEntity<CommentResponseDto> updateCommentById(@PathVariable Long comId,
                                                                @PathVariable Long userId,
                                                                @PathVariable Long eventId,
                                                                @RequestBody @Valid CommentRequestDto comment) {
        log.info("Получен запрос PATCH /comments/{}/users/{}/events/{}" +
                " на обновление комментария: {}", comId, userId, eventId, comment);
        return new ResponseEntity<>(service.updateComment(userId, eventId, comId, comment), HttpStatus.OK);
    }

    @DeleteMapping("{comId}/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long comId,
                                  @PathVariable Long userId,
                                  @PathVariable Long eventId) {
        log.info("Получен запрос DELETE /comments/{}/users/{}/events/{} на удаление", comId, userId, eventId);
        service.deleteComment(userId, eventId, comId);
    }
}
