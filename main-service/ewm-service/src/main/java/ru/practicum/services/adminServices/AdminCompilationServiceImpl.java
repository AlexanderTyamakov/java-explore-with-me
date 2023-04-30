package ru.practicum.services.adminServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.Compilation.CompilationDto;
import ru.practicum.dto.Compilation.NewCompilationDto;
import ru.practicum.dto.Compilation.UpdateCompilationRequestDto;
import ru.practicum.dto.mapper.CompilationMapper;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toEntity(newCompilationDto);
        compilation.setEvents(findEvents(newCompilationDto.getEvents()));
        try {
            compilation = compilationRepository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add compilation: {}", compilation.getTitle());
        return CompilationMapper.toDto(compilation);
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        if (compilationRepository.existsById(compId)) {
            log.info("Deleted compilation with id = {}", compId);
            compilationRepository.deleteById(compId);
        }
    }

    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequestDto dto) {
        Compilation compilationTarget = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation not found with id = %s", compId)));

        BeanUtils.copyProperties(dto, compilationTarget, "events", "pinned", "title");

        compilationTarget.setEvents(findEvents(dto.getEvents()));
        try {
            compilationRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Update category: {}", compilationTarget.getTitle());
        return CompilationMapper.toDto(compilationTarget);
    }

    private Set<Event> findEvents(Set<Long> eventsId) {
        if (eventsId == null) {
            return Set.of();
        }
        return eventRepository.findAllByIdIn(eventsId);
    }
}
