package ru.practicum.dto.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.enums.Status.CONFIRMED;
import static ru.practicum.enums.Status.PENDING;


@UtilityClass
public final class RequestMapper {

    public static Request toRequest(Event event, User requester) {
        return Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(event.getRequestModeration() ? PENDING : CONFIRMED)
                .build();
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request entity) {
        return ParticipationRequestDto.builder()
                .id(entity.getId())
                .requester(entity.getRequester().getId())
                .created(entity.getCreated())
                .event(entity.getEvent().getId())
                .status(entity.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }
}
