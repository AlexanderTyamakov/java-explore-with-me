package ru.practicum.dto.request;

import lombok.*;
import ru.practicum.enums.State;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamAdminForEvent {

    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private int from;
    private int size;
}
