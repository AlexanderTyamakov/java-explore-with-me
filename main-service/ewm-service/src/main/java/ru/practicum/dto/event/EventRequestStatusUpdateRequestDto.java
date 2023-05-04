package ru.practicum.dto.event;

import lombok.*;
import ru.practicum.enums.Status;
import ru.practicum.utils.ValidStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequestStatusUpdateRequestDto {
    @NotNull
    private List<Long> requestIds;

    @NotNull
    @ValidStatus
    private Status status;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequest{" +
                "requestIds=" + requestIds.toString() +
                ", status='" + status + '\'' +
                '}';
    }
}
