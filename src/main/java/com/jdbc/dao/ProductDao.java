package com.jdbc.dao;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.model.Category;
import com.jdbc.model.Product;



import java.sql.PreparedStatement;

public class ProductDao extends Dao<Product> {

    public ProductDao(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public Optional<Product> get(long id) {

        beginTx();
        String selectProduct = "SELECT * from products WHERE id =?";

        List<Product> products = executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeQuery();
        }, selectProduct, Product.class);

        if (products.isEmpty()) {
            return Optional.empty();
        } else {
            Product product = products.get(0);
            String selectCategory = "SELECT c.* from products p " +
                    "join category_product cp on (p.id = cp.product_id) and p.id =? " +
                    "join categories c on(cp.category_id = c.id)";
            List<Category> categories = executeQuery(statement -> {
                statement.setLong(1, id);
                return statement.executeQuery();
            }, selectCategory, Category.class);

            product.setCategories(new HashSet<>(categories));

            endTx();

            return Optional.of(product);
        }
    }

    @Override
    public List<Product> getAll() {
        beginTx();
        String selectProduct = "SELECT * from products";

        List<Product> products = executeQuery(PreparedStatement::executeQuery, selectProduct, Product.class);

        for (Product product : products) {
            String selectCategory = "SELECT c.* from products p " +
                    "join category_product cp on (p.id = cp.product_id) and p.id =? " +
                    "join categories c on(cp.category_id = c.id)";
            List<Category> categories = executeQuery(statement -> {
                statement.setLong(1, product.getId());
                return statement.executeQuery();
            }, selectCategory, Category.class);

            product.setCategories(new HashSet<>(categories));
        }

        endTx();

        return products;
    }

    @Override
    public void save(Product product) {
        beginTx();

        String insertProduct = "INSERT INTO products (id, name, price, description) value (?, ?, ?, ?)";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.executeUpdate();
        }, insertProduct);

        checkCategories(product);

        endTx();
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products set name = ?, price = ?, description =? where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(4, product.getId());
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.executeUpdate();
        }, sql);
        checkCategories(product);
    }

    @Override
    public void delete(Product product) {
        String sql = "DELETE from categories where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, product.getId());
            preparedStatement.executeUpdate();
        }, sql);
    }

    private void checkCategories(Product product) {
        CategoryDao categoryDao = new CategoryDao(getConnectionFactory());
        String insertRelation = "INSERT INTO category_product (category_id, product_id) value (?, ?)";

        for (Category category : product.getCategories()) {
            categoryDao.get(category.getId()).ifPresentOrElse(null, () -> categoryDao.save(category));

            executeQuery(preparedStatement -> {
                preparedStatement.setLong(1, category.getId());
                preparedStatement.setLong(2, product.getId());
                preparedStatement.executeUpdate();
            }, insertRelation);
        }
    }
}
