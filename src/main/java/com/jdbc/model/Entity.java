package com.jdbc.model;

import com.jdbc.sql.SqlFunction;

public interface Entity {
    void setParameters(SqlFunction<?, String> parameters);
}
