package ru.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    @Transactional
    EndpointHitDto save(EndpointHitDto dto);
}
