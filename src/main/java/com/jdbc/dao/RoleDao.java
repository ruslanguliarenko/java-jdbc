package com.jdbc.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.model.Permission;
import com.jdbc.model.Role;

import java.sql.PreparedStatement;

public class RoleDao extends Dao<Role> {

    public RoleDao(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public Optional<Role> get(long id) {
        beginTx();
        String selectProduct = "SELECT * from roles WHERE id =?";

        List<Role> roles = executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeQuery();
        }, selectProduct, Role.class);
        if(roles.isEmpty()){
            return Optional.empty();
        }else {

            Role role = roles.get(0);
            String selectCategory = "SELECT p.* from roles r " +
                    "join role_permission rp on (r.id = rp.role_id) and r.id =? " +
                    "join permissions p on(rp.permission_id = r.id)";
            List<Permission> categories = executeQuery(statement -> {
                statement.setLong(1, id);
                return statement.executeQuery();
            }, selectCategory, Permission.class);

            role.setPermissions(new HashSet<>(categories));

            endTx();

            return Optional.of(role);
        }
    }

    @Override
    public List<Role> getAll() {
        beginTx();
        String selectProduct = "SELECT * from roles";

        List<Role> roles = executeQuery(PreparedStatement::executeQuery, selectProduct, Role.class);

        for(Role role : roles) {
            String selectCategory = "SELECT p.* from roles r " +
                    "join role_permission rp on (r.id = rp.role_id) and r.id =? " +
                    "join permissions p on(rp.permission_id = r.id)";
            List<Permission> categories = executeQuery(statement -> {
                statement.setLong(1, role.getId());
                return statement.executeQuery();
            }, selectCategory, Permission.class);

            role.setPermissions(new HashSet<>(categories));
        }

        endTx();

        return roles;
    }

    @Override
    public void save(Role role) {
        beginTx();

        String insertProduct = "INSERT INTO roles (id, name) value (?, ?)";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, role.getId());
            preparedStatement.setString(2, role.getName());
            preparedStatement.executeUpdate();
        }, insertProduct);

        checkPermission(role);

        endTx();
    }

    @Override
    public void update(Role role) {
        String sql = "UPDATE roles set name = ? where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(2, role.getId());
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();
        }, sql);

        checkPermission(role);
    }

    @Override
    public void delete(Role role) {
        String sql = "DELETE from roles where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, role.getId());
            preparedStatement.executeUpdate();
        }, sql);
    }

    private void checkPermission(Role role){
        PermissionDao permissionDao = new PermissionDao(getConnectionFactory());
        String insertRelation = "INSERT INTO role_permission (role_id, permission_id) value (?, ?)";


        for(Permission permission : role.getPermissions()) {

            permissionDao.get(permission.getId()).ifPresentOrElse(null, () ->permissionDao.save(permission));


            executeQuery(preparedStatement -> {
                preparedStatement.setLong(1, permission.getId());
                preparedStatement.setLong(2, role.getId());
                preparedStatement.executeUpdate();
            }, insertRelation);
        }
    }
}
