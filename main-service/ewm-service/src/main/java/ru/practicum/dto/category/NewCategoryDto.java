package ru.practicum.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCategoryDto {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Override
    public String toString() {
        return "NewCategoryDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
