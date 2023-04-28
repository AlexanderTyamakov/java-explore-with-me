package ru.practicum.dto.user;

import lombok.*;
import ru.practicum.utils.NotBlankNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequestDto {
    @NotBlank
    @Size(max = 128)
    private String name;
    @Email
    @NotBlankNull
    @Size(max = 255)
    private String email;
}
