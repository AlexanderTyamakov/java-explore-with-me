package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Compilation;
import ru.practicum.utils.MyPageRequest;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinned(Boolean pinned, MyPageRequest pageable);

}
