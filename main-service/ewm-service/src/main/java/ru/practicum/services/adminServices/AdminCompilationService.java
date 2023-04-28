package ru.practicum.services.adminServices;


import ru.practicum.dto.Compilation.CompilationDto;
import ru.practicum.dto.Compilation.NewCompilationDto;
import ru.practicum.dto.Compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
