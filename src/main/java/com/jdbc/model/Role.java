package com.jdbc.model;


import java.util.Set;

import com.jdbc.sql.SqlFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Role implements Entity {

  private long id;
  private String name;
  private Set<Permission> permissions;

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
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

  @Override
  public void setParameters(SqlFunction<?, String> parameters) {
    setId((Long) parameters.apply("id"));
    setName((String) parameters.apply("name"));
  }

}
