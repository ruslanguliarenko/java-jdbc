package com.jdbc.sql;


import com.jdbc.exceptions.FailTxException;

import java.sql.SQLException;

public interface SqlFunction<R, T> {

    R execute(T t) throws SQLException;

    default R apply(T t){
        try {
           return execute(t);
        }catch (SQLException e){
            throw new FailTxException("Tx failed!!!");
        }
    }
}
