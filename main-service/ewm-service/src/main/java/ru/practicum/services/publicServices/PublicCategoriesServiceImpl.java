package ru.practicum.services.publicServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.Category.CategoryDto;
import ru.practicum.dto.mapper.CategoryMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoriesRepository;
import ru.practicum.utils.Pagination;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicCategoriesServiceImpl implements PublicCategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        Pagination pageable = new Pagination(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = categoriesRepository.findAll(pageable).toList();
        log.info("List of categories received");
        return CategoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDto get(Long catId) {
        final Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category not found with id = %s", catId)));
        log.info("Get Category: {}", category.getName());
        return CategoryMapper.toDto(category);
    }
}
