package ru.practicum.services.adminServices;


import ru.practicum.dto.Compilation.CompilationDto;
import ru.practicum.dto.Compilation.NewCompilationDto;
import ru.practicum.dto.Compilation.UpdateCompilationRequestDto;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequestDto updateCompilationRequestDto);
}
