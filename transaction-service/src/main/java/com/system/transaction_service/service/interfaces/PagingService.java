package com.system.transaction_service.service.interfaces;

import org.springframework.data.domain.Sort;

import java.util.Set;

public interface PagingService {

    Set<String> getAllFields(final Class<?> type);

    Sort.Direction getSortDirection(String direction);

    boolean checkPropertyPresent(final Set<String> properties, final String propertyName);
}
