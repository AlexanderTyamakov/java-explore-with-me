package ru.practicum.dto.compilation;

import lombok.*;
import ru.practicum.utils.NotBlankNull;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequestDto {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlankNull
    @Size(max = 128)
    private String title;
}
