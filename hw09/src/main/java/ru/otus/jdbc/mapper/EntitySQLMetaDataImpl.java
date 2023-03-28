package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private String selectAllSql;

    private String selectByIdSql;

    private String insertSql;

    private String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.selectAllSql = generateSelectAllSql(entityClassMetaData);
        this.selectByIdSql = generateSelectByIdSql(entityClassMetaData);
        this.insertSql = generateInsertSql(entityClassMetaData);
        this.updateSql = generateUpdateSql(entityClassMetaData);
    }

    private String generateUpdateSql(EntityClassMetaData entityClassMetaData) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("UPDATE ").append(entityClassMetaData.getName()).append(" set");

        List<Field> allFields = entityClassMetaData.getFieldsWithoutId();

        for (Field field : allFields) {
            sb.append(" ").append(field.getName()).append(" = ?,");
        }

        sb.deleteCharAt(sb.length() - 1);

        sb.append(" WHERE ").append(entityClassMetaData.getIdField().getName()).append(" = ?");

        return sb.toString();
    }

    private String generateInsertSql(EntityClassMetaData<?> entityClassMetaData) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("INSERT INTO ").append(entityClassMetaData.getName()).append(" (");

        List<Field> allFields = entityClassMetaData.getFieldsWithoutId();

        for (Field field : allFields) {
            sb.append(field.getName()).append(", ");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        sb.append(") VALUES (");

        for (int i = 0; i < allFields.size(); i++) {
            sb.append("?, ");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");

        return sb.toString();
    }

    private String generateSelectByIdSql(EntityClassMetaData<?> entityClassMetaData) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("SELECT");
        for (Field field : entityClassMetaData.getAllFields()) {
            sb.append(" ").append(field.getName()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" FROM ").append(entityClassMetaData.getName());
        sb.append(" WHERE ").append(entityClassMetaData.getIdField().getName()).append(" = ?");
        return sb.toString();
    }

    private String generateSelectAllSql(EntityClassMetaData<?> entityClassMetaData) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("SELECT");
        for (Field field : entityClassMetaData.getAllFields()) {
            sb.append(" ").append(field.getName()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" FROM ").append(entityClassMetaData.getName());
        return sb.toString();
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
