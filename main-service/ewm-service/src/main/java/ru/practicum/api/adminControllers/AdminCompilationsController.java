package ru.practicum.api.adminControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.Compilation.CompilationDto;
import ru.practicum.dto.Compilation.NewCompilationDto;
import ru.practicum.dto.Compilation.UpdateCompilationRequestDto;
import ru.practicum.services.adminServices.AdminCompilationService;


import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationService service;

    @PostMapping()
    public ResponseEntity<CompilationDto> save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос POST /admin/compilations c новой подборкой: {}", newCompilationDto.getTitle());
        return new ResponseEntity<>(service.save(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> delete(@PathVariable Long compId) {
        log.info("Получен запрос DELETE /admin/compilations/{}", compId);
        service.delete(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable Long compId,
                                                 @RequestBody @Valid UpdateCompilationRequestDto updateCompilationRequestDto) {
        log.info("Получен запрос PATCH /admin/compilations/{} на изменение подборки.", compId);
        return new ResponseEntity<>(service.update(compId, updateCompilationRequestDto), HttpStatus.OK);
    }
}
