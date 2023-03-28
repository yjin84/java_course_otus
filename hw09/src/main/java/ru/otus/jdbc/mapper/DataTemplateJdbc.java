package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<?> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<?> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                T resultObject = null;
                if (rs.next()) {
                    var fields = entityClassMetaData.getAllFields();
                    var parameters = fields.stream().map(field -> {
                        try {
                            return rs.getObject(field.getName());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray();
                    resultObject = (T) entityClassMetaData.getConstructor().newInstance(parameters);
                }
                return resultObject;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var objectList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    var fields = entityClassMetaData.getAllFields();
                    var parameters = fields.stream().map(field -> {
                        try {
                            return rs.getObject(field.getName());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray();
                    objectList.add((T) entityClassMetaData.getConstructor().newInstance(parameters));
                }
                return objectList;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            var fields = entityClassMetaData.getFieldsWithoutId();
            var parameters = new ArrayList<>(fields.size());

            for (Field field : fields) {
                field.setAccessible(true);
                parameters.add(field.get(client));
            }

            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), parameters);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            var fields = entityClassMetaData.getFieldsWithoutId();
            var parameters = new ArrayList<>(fields.size());

            for (Field field : fields) {
                field.setAccessible(true);
                parameters.add(field.get(client));
            }

            var fieldId = entityClassMetaData.getIdField();
            fieldId.setAccessible(true);

            parameters.add(fieldId.get(client));

            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), parameters);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
