package ru.otus.jdbc.mapper;

import ru.otus.jdbc.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataFactory {
    public EntityClassMetaData createOf(Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class must not be null");
        }
        String name = clazz.getSimpleName();

        Field[] declaredFields = clazz.getDeclaredFields();

        Class[] fieldTypes = new Class[declaredFields.length];

        List<Field> withoutIdFields = new ArrayList<>(declaredFields.length - 1);

        Field idField = null;

        int indx = 0;
        for (Field field : declaredFields) {
            fieldTypes[indx] = field.getType();

            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            } else {
                withoutIdFields.add(field);
            }

            indx++;
        }

        if (idField == null) {
            throw new IllegalStateException("id field must be mark with Id annotation");
        }

        Constructor constructor;
        try {
            constructor = clazz.getConstructor(fieldTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return new EntityClassMetaDataImpl(name, constructor, idField, Arrays.asList(declaredFields), withoutIdFields);
    }
}
