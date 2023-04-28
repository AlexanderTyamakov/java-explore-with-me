package ru.practicum.services.publicServices;

import ru.practicum.dto.Category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(Long catId);
}
