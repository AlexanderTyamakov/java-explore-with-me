package ru.practicum.services.adminServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.Category.CategoryDto;
import ru.practicum.dto.Category.NewCategoryDto;
import ru.practicum.dto.mapper.CategoryMapper;
import ru.practicum.exception.ConditionsNotMetException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoriesRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.utils.UtilMergeProperty;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCategoriesServiceImpl implements AdminCategoriesService {

    private final CategoriesRepository categoriesRepository;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto dto) {
        Category category = CategoryMapper.toEntity(dto);
        try {
            category = categoriesRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add category: {}", category.getName());
        return CategoryMapper.toDto(category);
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        if (eventRepository.existsByCategory(get(catId))) {
            throw new ConditionsNotMetException("The category is not empty");
        } else {
            log.info("Deleted category with id = {}", catId);
            categoriesRepository.deleteById(catId);
        }
    }

    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto dto, Long catId) {
        Category categoryUpdate = CategoryMapper.toEntity(dto);
        Category categoryTarget = get(catId);

        try {
            UtilMergeProperty.copyProperties(categoryUpdate, categoryTarget);
            categoriesRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Update category: {}", categoryTarget.getName());
        return CategoryMapper.toDto(categoryTarget);
    }

    private Category get(Long id) {
        final Category category = categoriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category not found with id = %s", id)));
        log.info("Get category: {}", category.getName());
        return category;
    }

}
