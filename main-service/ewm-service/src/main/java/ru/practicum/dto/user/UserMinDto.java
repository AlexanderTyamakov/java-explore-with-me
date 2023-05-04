package ru.practicum.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMinDto {

    private Long id;

    private String name;
}
