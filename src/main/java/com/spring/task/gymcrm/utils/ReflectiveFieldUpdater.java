package com.spring.task.gymcrm.utils;

import lombok.AllArgsConstructor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class ReflectiveFieldUpdater<T, V> {
    private final T entity;
    private final V updateDto;

    public void updateFields() {
        validateUpdateAnnotation(entity, updateDto);

        Map<String, Field> entityFieldsMap = getFieldsMap(entity);
        Map<String, Field> updateFieldsMap = getFieldsMap(updateDto);

        updateFieldsMap.forEach((fieldName, value) -> {
            if (entityFieldsMap.containsKey(fieldName)) {
                Field entityField = entityFieldsMap.get(fieldName);
                Field updateField = updateFieldsMap.get(fieldName);
                checkUpdateCompatibility(entityField, updateField);
                updateField(entityField, updateField);
            }
        });
    }

    private void validateUpdateAnnotation(T entity, V updateDto) {
        UpdateDto updateAnnotation = updateDto.getClass().getAnnotation(UpdateDto.class);
        if (updateAnnotation != null) {
            Class<?> updatesClass = updateAnnotation.updatesClass();
            if (!updatesClass.equals(entity.getClass())) {
                throw new IllegalArgumentException("Failed to begin update! Update object class " + updateDto.getClass() + " is not annotated as UpdateDto for " + entity.getClass() + "!");
            }
        } else {
            throw new IllegalArgumentException("Failed to begin update! Update object is not annotated as UpdateDto!");
        }
    }

    private static Map<String, Field> getFieldsMap(Object object) {
        List<Field> entityFields = Arrays.stream(object.getClass().getDeclaredFields()).toList();
        HashMap<String, Field> entityFieldsMap = new HashMap<>();
        for (Field field : entityFields) {
            entityFieldsMap.put(field.getName(), field);
        }
        return entityFieldsMap;
    }

    private void updateField(Field entityField, Field updateField) {
        if (!differentValues(entityField, updateField)) {
            return;
        }
        try {
            updateField.setAccessible(true);
            entityField.setAccessible(true);
            entityField.set(entity, updateField.get(updateDto));
        } catch (IllegalAccessException e) {
            //TODO write specific exception here
            throw new RuntimeException(e);
        }
    }

    private boolean differentValues(Field entityField, Field updateField) {
        try {
            Object entityFieldValue = entityField.get(entity);
            Object updateFieldValue = updateField.get(updateDto);
            return entityFieldValue.equals(updateFieldValue);
        } catch (IllegalAccessException e) {
            //TODO write specific exception here
            throw new RuntimeException(e);
        }
    }

    private void checkUpdateCompatibility(Field entityField, Field update) {
        if (!entityField.getType().equals(update.getType())) {
            throw new IllegalArgumentException("Failed to update " + entity.getClass() + " with " + updateDto.getClass() +
                    ": entity field " + entityField.getName() + " " + entityField.getClass() +
                    " can not be updated with an object of " + update.getClass() + " class!");
        }
    }
}
