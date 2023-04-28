package ru.practicum.services.adminServices;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.dto.request.RequestParamAdminForEventDto;

import java.util.List;

public interface AdminEventsService {
    EventFullDto update(Long eventId, UpdateEventAdminRequestDto updateEvent);

    List<EventFullDto> getAll(RequestParamAdminForEventDto param);
}
