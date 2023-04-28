package ru.practicum.services.adminServices;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.request.RequestParamAdminForEvent;

import java.util.List;

public interface AdminEventsService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamAdminForEvent param);
}
