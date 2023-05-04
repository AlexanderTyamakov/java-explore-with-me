package ru.practicum.dto.comments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
    @Size(min = 1, max = 250)
    @NotBlank
    private String text;
}
