package ru.practicum.api.adminControllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comments.CommentRequestDto;
import ru.practicum.dto.comments.CommentResponseDto;
import ru.practicum.services.privareServices.PrivateCommentsService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/comments")
@Validated
public class AdminCommentsController {

    public final PrivateCommentsService service;

    @PatchMapping("{comId}/users/{userId}/events/{eventId}")
    public ResponseEntity<CommentResponseDto> updateCommentById(@PathVariable Long comId,
                                                                @PathVariable Long userId,
                                                                @PathVariable Long eventId,
                                                                @RequestBody @Valid CommentRequestDto comment) {
        log.info("Получен запрос PATCH /admin/comments/{}/users/{}/events/{}" +
                " на обновление комментария: {}", comId, userId, eventId, comment);
        return new ResponseEntity<>(service.updateComment(userId, eventId, comId, comment), HttpStatus.OK);
    }

    @DeleteMapping("{comId}/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long comId,
                                  @PathVariable Long userId,
                                  @PathVariable Long eventId) {
        log.info("Получен запрос DELETE /admin/comments/{}/users/{}/events/{} на удаление", comId, userId, eventId);
        service.deleteComment(userId, eventId, comId);
    }


}
