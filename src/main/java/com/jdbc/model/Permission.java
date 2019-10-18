package com.jdbc.model;


import com.jdbc.sql.SqlFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Permission implements Entity{

  private long id;
  private String name;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setParameters(SqlFunction<?, String> parameters) {
    setId((Long) parameters.apply("id"));
    setName((String) parameters.apply("name"));
  }

}
