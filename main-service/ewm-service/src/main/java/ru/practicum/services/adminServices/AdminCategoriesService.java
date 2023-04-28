package ru.practicum.services.adminServices;

import ru.practicum.dto.Category.CategoryDto;
import ru.practicum.dto.Category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);
}
