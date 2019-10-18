package com.jdbc.dao;

import java.util.List;
import java.util.Optional;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.model.Permission;

import java.sql.PreparedStatement;

public class PermissionDao extends Dao<Permission> {


    public PermissionDao(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public Optional<Permission> get(long id) {

        String sql = "SELECT * from permissions WHERE id =?";

        List<Permission> permissions = executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeQuery();
        }, sql, Permission.class);

        return getEntity(permissions);
    }

    @Override
    public List<Permission> getAll() {

        String sql = "SELECT * from permissions";

        return executeQuery(PreparedStatement::executeQuery, sql, Permission.class);
    }

    @Override
    public void save(Permission permission) {
        String sql = "INSERT INTO permissions (id, name) value (?, ?)";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, permission.getId());
            preparedStatement.setString(2, permission.getName());
            preparedStatement.executeUpdate();
        }, sql);

    }

    @Override
    public void update(Permission permissions) {
        String sql = "UPDATE permissions set name = ? where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(2, permissions.getId());
            preparedStatement.setString(1, permissions.getName());
            preparedStatement.executeUpdate();
        }, sql);
    }

    @Override
    public void delete(Permission permission) {
        String sql = "DELETE from permissions where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, permission.getId());
            preparedStatement.executeUpdate();
        }, sql);
    }

}
