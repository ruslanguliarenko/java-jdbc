package com.jdbc.model;



import com.csv.annotation.Parsed;
import com.jdbc.sql.SqlFunction;

public class Category implements Entity {

  private Integer id;

  private String name;

  public Category(){}

  public int getId() {
    return id;
  }

  public void setId(int id) {
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
    setId((Integer) parameters.apply("id"));
    setName((String) parameters.apply("name"));
  }

}
