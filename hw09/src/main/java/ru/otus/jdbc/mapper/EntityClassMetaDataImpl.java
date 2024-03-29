package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    
    private final String name;
    
    private final Constructor<T> constructor;

    private final Field idField;

    private final List<Field> allFields;

    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(String name, Constructor<T> constructor, Field idField, List<Field> allFields, List<Field> fieldsWithoutId) {
        this.name = name;
        this.constructor = constructor;
        this.idField = idField;
        this.allFields = allFields;
        this.fieldsWithoutId = fieldsWithoutId;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
