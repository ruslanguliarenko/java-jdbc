package com.jdbc.model;


import java.util.Set;

import com.jdbc.sql.SqlFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements Entity {

  private long id;
  private String name;
  private String email;
  private Set<Role> roles;
  private Set<Order> orders;


  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<Order> getOrders() {
    return orders;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
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


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setParameters(SqlFunction<?, String> parameters){
    setId((Long) parameters.apply("id"));
    setName((String) parameters.apply("name"));
    setEmail((String) parameters.apply("email"));
  }
}
