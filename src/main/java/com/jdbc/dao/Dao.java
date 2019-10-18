package com.jdbc.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.jdbc.sql.ConnectionFactory;
import com.jdbc.sql.SqlConsumer;
import com.jdbc.sql.SqlFunction;
import com.jdbc.model.Entity;
import com.opencsv.bean.AbstractCsvConverter;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.sql.*;

public abstract class Dao<T> {
    private Connection connection;
    private final ConnectionFactory connectionFactory;

    private final SqlFunction<PreparedStatement, String> preparedStatementFactory = (sql) -> connection.prepareStatement(sql);
    private final SqlFunction<Boolean, ResultSet> checkNext = ResultSet::next;
    private final Consumer<SqlConsumer<Connection> > connectionConsumer = c -> c.accept(connection);


    public Dao(ConnectionFactory connectionFactory){
        this.connectionFactory = connectionFactory;
    }
    public abstract Optional<T> get(long id);

    abstract List<T> getAll();

    public abstract void save(T t);

    abstract void update(T t);

    abstract void delete(T t);

    public void beginTx(){
        connection = connectionFactory.getConnection();

        connectionConsumer.accept(c -> c.setAutoCommit(false));
    }

    public void endTx() {
        connectionConsumer.accept(Connection::commit);
        connectionConsumer.accept(Connection::close);
    }

    public void executeQuery(SqlConsumer<PreparedStatement> queryExecutor, String sql) {

        if (!isTx()) {
            connection = connectionFactory.getConnection();
    }

        PreparedStatement preparedStatement = preparedStatementFactory.apply(sql);
        queryExecutor.accept(preparedStatement);
    }

    public<T extends Entity> List<T> executeQuery(SqlFunction<ResultSet, PreparedStatement> queryExecutor, String sql, Class<T> entityType)  {

        if (!isTx()) {
            connection = connectionFactory.getConnection();
        }

        PreparedStatement preparedStatement = preparedStatementFactory.apply(sql);

        ResultSet resultSet = queryExecutor.apply(preparedStatement);
        List<T> entities = new ArrayList<>();

        while (checkNext.apply(resultSet)) {
            SqlFunction<?, String> parameters = resultSet::getObject;

            T entity = createEntity(entityType);
            entity.setParameters(parameters);
            entities.add(entity);
        }

        return entities;
    }

    private boolean isTx() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    private <T extends Entity> T createEntity(Class<T> entityType){
        try {
            return entityType.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException  e) {
            throw new RuntimeException();
        }
    }
    protected Optional<T> getEntity(List<T> entities){
        if(entities.isEmpty()){
            return Optional.empty();
        } else{
            return Optional.ofNullable(entities.get(0));
        }
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

}
