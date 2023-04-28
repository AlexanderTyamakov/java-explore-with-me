package ru.practicum.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.*;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.enums.State;
import ru.practicum.model.Event;
import ru.practicum.model.Location;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public final class EventMapper {

    public static Event toEntity(NewEventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(dto.getDescription())
                .date(dto.getEventDate())
                .location(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()))
                .paid(dto.isPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.isRequestModeration())
                .state(State.PENDING)
                .title(dto.getTitle())
                .build();
    }

    public static Event toEntity(UpdateEventAdminRequestDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(dto.getDescription())
                .date(dto.getEventDate())
                .location(dto.getLocation() != null ? new Location(dto.getLocation().getLat(),
                        dto.getLocation().getLon()) : null)
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }

    public static Event toEntity(UpdateEventUserRequestDtoDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .date(dto.getEventDate())
                .paid(dto.getPaid())
                .location(dto.getLocation() != null ? new Location(dto.getLocation().getLat(),
                        dto.getLocation().getLon()) : null)
                .participantLimit(dto.getParticipantLimit())
                .title(dto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event entity) {
        return EventFullDto.builder()
                .id(entity.getId())
                .annotation(entity.getAnnotation())
                .category(CategoryMapper.toDto(entity.getCategory()))
                .confirmedRequests(entity.getConfirmedRequests())
                .createdOn(entity.getCreatedOn())
                .description(entity.getDescription())
                .eventDate(entity.getDate())
                .initiator(UserMapper.toUserShortDto(entity.getInitiator()))
                .location(new LocationDto(entity.getLocation().getLat(), entity.getLocation().getLon()))
                .paid(entity.getPaid())
                .participantLimit(entity.getParticipantLimit())
                .publishedOn(entity.getPublishedOn())
                .requestModeration(entity.getRequestModeration())
                .state(entity.getState())
                .title(entity.getTitle())
                .views(entity.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event entity) {
        return EventShortDto.builder()
                .id(entity.getId())
                .annotation(entity.getAnnotation())
                .category(CategoryMapper.toDto(entity.getCategory()))
                .confirmedRequests(entity.getConfirmedRequests())
                .eventDate(entity.getDate())
                .initiator(UserMapper.toUserShortDto(entity.getInitiator()))
                .paid(entity.getPaid())
                .title(entity.getTitle())
                .views(entity.getViews())
                .build();
    }

    public static Set<EventShortDto> toEventShortDtoList(Set<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toSet());
    }
}
