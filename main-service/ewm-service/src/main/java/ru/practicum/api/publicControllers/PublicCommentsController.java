package ru.practicum.api.publicControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.dto.comments.CommentResponseDto;
import ru.practicum.services.publicServices.PublicCommentsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comments")
@Validated
public class PublicCommentsController {

    public final PublicCommentsService commentsService;

    @GetMapping("/{comId}/events/{eventId}")
    public ResponseEntity<CommentResponseDto> getEventCommentById(@PathVariable Long comId,
                                                                  @PathVariable Long eventId) {
        log.info("Получен запрос GET /comments/{}/events/{}", comId, eventId);
        return new ResponseEntity<>(commentsService.getComment(eventId, comId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<CommentResponseDto>> getEventComments(@PathVariable Long eventId,
                                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /comments/events/{}", eventId);
        return new ResponseEntity<>(commentsService.getAllComments(eventId, from, size), HttpStatus.OK);
    }
}
