package ru.practicum.services.publicServices;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.request.RequestParamPublicForEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface PublicEventsService {
    Set<EventShortDto> getAll(RequestParamPublicForEvent param);

    EventFullDto get(Long id, HttpServletRequest request);
}
