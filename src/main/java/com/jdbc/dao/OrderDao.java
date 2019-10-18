package com.jdbc.dao;

import java.util.List;
import java.util.Optional;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.model.Order;
import com.jdbc.model.Product;

import java.sql.PreparedStatement;

public class OrderDao extends Dao<Order> {

    public OrderDao(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public Optional<Order> get(long id) {
        beginTx();
        String selectOrder = "SELECT * from orders WHERE id =?";

        List<Order> orders = executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeQuery();
        }, selectOrder, Order.class);

        if(orders.isEmpty()){
            return Optional.empty();
        }else {
            Order order = orders.get(0);
            String selectProducts = "SELECT p.* from orders o" +
                    "join order_items oi on (o.id = oi.order_id) and o.id =? " +
                    "join products p on (p.id = oi.product_id)";

            List<Product> products = executeQuery(statement -> {
                statement.setLong(1, id);
                return statement.executeQuery();
            }, selectProducts, Product.class);

            order.setProducts(products);

            endTx();

            return Optional.of(order);
        }
    }

    @Override
    public List<Order> getAll() {
        beginTx();
        String selectOrder = "SELECT * from orders";

        List<Order> orders = executeQuery(PreparedStatement::executeQuery, selectOrder, Order.class);

        for(Order order : orders) {
            String selectProducts = "SELECT p.* from orders o" +
                    "join order_items oi on (o.id = oi.order_id) and o.id =? " +
                    "join products p on (p.id = oi.product_id)";

            List<Product> products = executeQuery(PreparedStatement::executeQuery, selectProducts, Product.class);

            order.setProducts(products);
        }
        endTx();

        return orders;
    }

    @Override
    public void save(Order order) {
        beginTx();

        String insertOrder = "INSERT INTO orders (id, user_id) value (?, ?)";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, order.getId());
            preparedStatement.setLong(2, order.getUser().getId());
            preparedStatement.executeUpdate();
        }, insertOrder);

        checkProduct(order);

        endTx();
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders set user_id = ? where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(4, order.getId());
            preparedStatement.setLong(1, order.getUser().getId());
            preparedStatement.executeUpdate();
        }, sql);

        checkProduct(order);
    }

    @Override
    public void delete(Order order) {
        String sql = "DELETE from orders where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, order.getId());
            preparedStatement.executeUpdate();
        }, sql);
    }

    private void checkProduct(Order order){
        ProductDao productDao = new ProductDao(getConnectionFactory());
        String insertRelation = "INSERT INTO order_items (order_id, product_id) value (?, ?)";


        for(Product product : order.getProducts()) {
            productDao.get(product.getId()).ifPresentOrElse(null, () ->productDao.save(product));

            executeQuery(preparedStatement -> {
                preparedStatement.setLong(1, order.getId());
                preparedStatement.setLong(2, product.getId());
                preparedStatement.executeUpdate();
            }, insertRelation);
        }
    }
}
