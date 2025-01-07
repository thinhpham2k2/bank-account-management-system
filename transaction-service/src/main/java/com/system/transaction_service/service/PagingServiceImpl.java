package com.system.transaction_service.service;

import com.system.transaction_service.service.interfaces.PagingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Service
public class PagingServiceImpl implements PagingService {

    @Override
    public Set<String> getAllFields(Class<?> type) {

        Set<String> fields = new HashSet<>();
        //loop the fields using Java Reflections
        for (Field field : type.getDeclaredFields()) {

            fields.add(field.getName());
        }
        //recursive call to getAllFields
        if (type.getSuperclass() != null) {

            fields.addAll(getAllFields(type.getSuperclass()));
        }

        return fields;
    }

    @Override
    public Sort.Direction getSortDirection(String direction) {

        if (direction.equals("asc")) {

            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {

            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @Override
    public boolean checkPropertyPresent(Set<String> properties, String propertyName) {

        return properties.contains(propertyName);
    }
}
