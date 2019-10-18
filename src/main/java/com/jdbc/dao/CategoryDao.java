package com.jdbc.dao;

import java.util.List;
import java.util.Optional;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.model.Category;

import java.sql.PreparedStatement;

public class CategoryDao  extends Dao<Category>  {

    public CategoryDao(ConnectionFactory connectionFactory){
        super(connectionFactory);
    }


    @Override
    public Optional<Category> get(long id) {

        String sql = "SELECT * from categories WHERE id =?";

        List<Category> categories = executeQuery(statement -> {
            statement.setLong(1, id);
            return statement.executeQuery();
        }, sql, Category.class);

        return getEntity (categories);
    }

    @Override
    public List<Category> getAll() {

        String sql = "SELECT * from categories";

        return executeQuery(PreparedStatement::executeQuery, sql, Category.class);
    }

    @Override
    public void save(Category category) {
        String sql = "INSERT INTO categories (id, name) value (?, ?)";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, category.getId());
            preparedStatement.setString(2, category.getName());
            preparedStatement.executeUpdate();
        }, sql);

    }

    @Override
    public void update(Category category) {
        String sql = "UPDATE categories set name = ? where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(2, category.getId());
            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();
        }, sql);
    }

    @Override
    public void delete(Category category) {
        String sql = "DELETE from categories where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, category.getId());
            preparedStatement.executeUpdate();
        }, sql);
    }

}
