package ru.practicum.dto.event;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequestDtoDto extends UpdateEventRequestDto {

    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW, CANCEL_REVIEW
    }

}
