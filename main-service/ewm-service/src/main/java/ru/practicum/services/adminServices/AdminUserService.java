package ru.practicum.services.adminServices;

import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);
}
