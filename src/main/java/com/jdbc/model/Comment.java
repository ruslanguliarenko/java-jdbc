package com.jdbc.model;


import java.util.Set;

import com.jdbc.sql.SqlFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Comment implements Entity {

  private long id;
  private String message;
  private long userId;
  private long commentId;
  private long productId;
  private Set<Comment> comments;

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }


  public long getCommentId() {
    return commentId;
  }

  public void setCommentId(long commentId) {
    this.commentId = commentId;
  }


  public long getProductId() {
    return productId;
  }

  public void setProductId(long productId) {
    this.productId = productId;
  }

  @Override
  public void setParameters(SqlFunction<?, String> parameters)  {
    setId((Long) parameters.apply("id"));
    setMessage((String) parameters.apply("message"));
    setUserId((Long) parameters.apply("user_id"));
    setCommentId((Long) parameters.apply("comment_id"));
    setProductId((Long) parameters.apply("product_id"));
  }

}
