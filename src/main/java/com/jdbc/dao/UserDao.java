package com.jdbc.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.model.Order;
import com.jdbc.model.Role;
import com.jdbc.model.User;

import java.sql.PreparedStatement;

public class UserDao extends Dao<User> {

    public UserDao(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public Optional<User> get(long id) {

        beginTx();
        String selectUser = "SELECT * from users WHERE id =?";

        List<User> users = executeQuery(statement -> {
            statement.setLong(1, id);
            return statement.executeQuery();
        }, selectUser, User.class);

        if(users.isEmpty()){
            return Optional.empty();
        }else {
            User user = users.get(0);
            String selectRole = "SELECT r.* from users u " +
                    "join user_role ur on (u.id = ur.user_id) and u.id =? " +
                    "join role r on(ur.role_id = r.id)";
            List<Role> roles = executeQuery(statement -> {
                statement.setLong(1, id);
                return statement.executeQuery();
            }, selectRole, Role.class);

            user.setRoles(new HashSet<>(roles));

            String selectOrder = "SELECT o.* from users u " +
                    "join order o on (u.id = o.user_id) and u.id =? ";
            List<Order> orders = executeQuery(preparedStatement -> {
                preparedStatement.setLong(1, id);
                return preparedStatement.executeQuery();
            }, selectOrder, Order.class);

            user.setOrders(new HashSet<>(orders));

            endTx();

            return Optional.of(user);
        }
    }

    @Override
    public List<User> getAll() {
        beginTx();
        String selectProduct = "SELECT * from products";

        List<User> users = executeQuery(PreparedStatement::executeQuery, selectProduct, User.class);

        for(User user : users) {
            String selectRole = "SELECT r.* from users u " +
                    "join user_role ur on (u.id = ur.user_id) and u.id =? " +
                    "join role r on(ur.role_id = r.id)";
            List<Role> roles = executeQuery(statement -> {
                statement.setLong(1, user.getId());
                return statement.executeQuery();
            }, selectRole, Role.class);

            user.setRoles(new HashSet<>(roles));

            String selectOrder = "SELECT o.* from users u " +
                    "join order o on (u.id = o.user_id) and u.id =? ";
            List<Order> orders = executeQuery(statement -> {
                statement.setLong(1, user.getId());
                return statement.executeQuery();
            }, selectOrder, Order.class);

            user.setOrders(new HashSet<>(orders));
        }

        endTx();

        return users;
    }

    @Override
    public void save(User user) {
        beginTx();

        String insertProduct = "INSERT INTO users (id, name, email) value (?, ?, ?)";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
        }, insertProduct);

        checkRoles(user);

        endTx();
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users set name = ?, email = ?, where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(4, user.getId());
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
        }, sql);

        checkRoles(user);
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE from users where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, user.getId());
            preparedStatement.executeUpdate();
        }, sql);
    }

    private void checkRoles(User user){
        RoleDao roleDao = new RoleDao(getConnectionFactory());
        String insertRelation = "INSERT INTO user_role (user_id, role_id) value (?, ?)";


        for(Role role : user.getRoles()) {

            roleDao.get(role.getId()).ifPresentOrElse(null, ()->roleDao.save(role));

            executeQuery(preparedStatement -> {
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setLong(2, role.getId());
                preparedStatement.executeUpdate();
            }, insertRelation);
        }
    }

    private void checkOrders(User user){
        OrderDao orderDao = new OrderDao(getConnectionFactory());
        for(Order order : user.getOrders()) {
            orderDao.get(order.getId()).ifPresentOrElse(null, () -> orderDao.save(order));
        }
    }
}
