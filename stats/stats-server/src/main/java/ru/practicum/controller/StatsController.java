package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.ErrorResponse;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> get(@RequestParam
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                  @RequestParam
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                  @RequestParam(required = false) List<String> uris,
                                                  @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получен запрос GET /stats");
        return new ResponseEntity<>(service.get(start, end, uris, unique), HttpStatus.OK);
    }


    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> save(@RequestBody EndpointHitDto dto) {
        log.info("Получен запрос POST /hit");
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final RuntimeException e) {
        return new ErrorResponse(
                "Возникла ошибка", e.getMessage()
        );
    }
}
