package com.jdbc.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.csv.annotation.Nested;
import com.csv.annotation.Parsed;
import com.jdbc.dao.Dao;
import com.jdbc.sql.SqlFunction;



public class Product implements Entity{

  @Parsed
  private long id;
  @Parsed
  private String name;
  @Parsed
  private String description;
  @Parsed
  private double price;
  @Nested
  private Set<Category> categories;


  public Set<Category> getCategories() {
    return categories;
  }
  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

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


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public void setParameters(SqlFunction<?, String> parameters) {
      setId((Long) parameters.apply("id"));
      setName((String) parameters.apply("name"));
      setPrice((Double) parameters.apply("price"));
      setDescription((String) parameters.apply("description"));
  }

}
