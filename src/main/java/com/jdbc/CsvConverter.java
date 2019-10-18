package com.jdbc;

import com.jdbc.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CsvConverter {
    private ConnectionFactory connectionFactory;
    public CsvConverter(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    public  void saveInDB(String path, String table) throws SQLException {
        String sql = "LOAD DATA  INFILE ? \n" +
                "INTO TABLE tableName \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'";

        sql = sql.replace("tableName", table);
        Connection connection = connectionFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, path);
        preparedStatement.executeUpdate();

    }

    public  void readFromDB(String path, String table) throws SQLException {
        String sql = "SELECT * FROM\n" +
                "    tableName INTO OUTFILE ? \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'";

        sql = sql.replace("tableName", table);
        Connection connection = connectionFactory.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, path);
        preparedStatement.execute();
 
    }
}
