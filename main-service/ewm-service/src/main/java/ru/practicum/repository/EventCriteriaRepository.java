package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.EventSearchCriteria;

@Repository
public interface EventCriteriaRepository {

    Page<Event> findAllWithFilters(Pageable pageable, EventSearchCriteria eventSearchCriteria);
}
