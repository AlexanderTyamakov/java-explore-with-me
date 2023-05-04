package ru.practicum.dto.comments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.dto.user.UserMinDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String text;
    private UserMinDto author;
    private Long eventId;
}