package com.jdbc.sql;

import com.jdbc.exceptions.FailTxException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlConsumer<T>{
    void execute(T t) throws SQLException;
    default void accept(T t) {
        try {
            execute(t);
        }catch (SQLException e){
            throw new FailTxException("Tx failed!!!");
        }
    }
}
