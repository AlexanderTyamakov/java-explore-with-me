package ru.practicum.api.privateControllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comments.CommentRequestDto;
import ru.practicum.dto.comments.CommentResponseDto;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.services.privareServices.PrivateEventsService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {

    public final PrivateEventsService service;

    @GetMapping
    public ResponseEntity<Set<EventShortDto>> getAll(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос GET /users{}/events c параметрами: from = {}, size = {}", userId, from, size);
        return new ResponseEntity<>(service.getAll(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> get(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Получен запрос GET /users{}/events/{}", userId, eventId);
        return new ResponseEntity<>(service.get(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable Long userId,
                                                                     @PathVariable Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        return new ResponseEntity<>(service.getRequests(userId, eventId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PathVariable Long userId,
                                               @RequestBody @Valid NewEventDto eventDto) {
        log.info("Получен запрос POST /users/{}/events c новым событием: {}", userId, eventDto);
        return new ResponseEntity<>(service.create(userId, eventDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@PathVariable Long userId, @PathVariable Long eventId,
                                               @RequestBody @Valid UpdateEventUserRequestDtoDto eventDto) {
        log.info("Получен запрос PATCH /users/{}/events/{eventId}" +
                " c обновлённым событием id = {}: {}", userId, eventId, eventDto);
        return new ResponseEntity<>(service.update(userId, eventId, eventDto), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResultDto> updateRequestStatus(@PathVariable Long userId,
                                                                                 @PathVariable Long eventId,
                                                                                 @Validated @RequestBody EventRequestStatusUpdateRequestDto request) {
        log.info("Получен запрос PATCH /users/{}/events/{eventId}/requests" +
                " на обновление статуса события id = {}: {}", userId, eventId, request);
        return new ResponseEntity<>(service.updateRequestStatus(userId, eventId, request), HttpStatus.OK);
    }

    @PostMapping("/{eventId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody @Valid CommentRequestDto comment) {
        log.info("Получен запрос POST /users/{}/events/{}/comments c новым комментарием: {}", userId, eventId, comment);
        return new ResponseEntity<>(service.addComment(userId, eventId, comment), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}/comments/{comId}")
    public ResponseEntity<CommentResponseDto> updateCommentById(@PathVariable Long userId,
                                                                @PathVariable Long eventId,
                                                                @PathVariable Long comId,
                                                                @RequestBody @Valid CommentRequestDto comment) {
        log.info("Получен запрос PATCH /users/{}/events/{}/comments" +
                " на обновление комментария id = {}: {}", userId, eventId, comId, comment);
        return new ResponseEntity<>(service.updateComment(userId, eventId, comId, comment), HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}/comments/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @PathVariable Long comId) {
        log.info("Получен запрос DELETE /users/{}/events/{}/comments/{} на удаление", userId, eventId, comId);
        service.deleteComment(userId, eventId, comId);
    }
}
