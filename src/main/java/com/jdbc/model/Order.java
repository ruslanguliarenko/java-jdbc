package com.jdbc.model;


import java.util.List;
import java.util.Objects;

import com.jdbc.sql.SqlFunction;


public class Order implements Entity {

  private long id;
  private User user;
  private List<Product> products;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  @Override
  public void setParameters(SqlFunction<?, String> parameters) {
    setId((Long) parameters.apply("id"));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(user, order.user) &&
            Objects.equals(products, order.products);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, products);
  }
}
