package ru.practicum.services.adminServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.mapper.UserMapper;
import ru.practicum.dto.user.NewUserRequestDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.utils.Pagination;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        List<User> users;
        Pagination pageable = new Pagination(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, pageable);
        }
        log.info("Number of users: {}", users.size());
        return UserMapper.toUserDtoList(users);
    }

    @Transactional
    @Override
    public UserDto save(NewUserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Provided email is occupied", e);
        }
        log.info("Add user: {}", user.getEmail());
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        if (userRepository.existsById(userId)) {
            log.info("Deleted user with id = {}", userId);
            userRepository.deleteById(userId);
        }
    }
}
