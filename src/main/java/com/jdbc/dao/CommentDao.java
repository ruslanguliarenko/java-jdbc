package com.jdbc.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.model.Comment;

import java.sql.PreparedStatement;

public class CommentDao extends Dao<Comment> {

    public CommentDao(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public Optional<Comment> get(long id) {
        String sql = "SELECT * from comments WHERE id =?";

        List<Comment> comments = executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeQuery();
        }, sql, Comment.class);

        return getEntity(comments);
    }

    @Override
    public List<Comment> getAll() {
        String sql = "SELECT * from comment";
        beginTx();

        List<Comment> comments = executeQuery(PreparedStatement::executeQuery, sql, Comment.class);

        for(Comment comment : comments) {
            String selectCategory = "SELECT c.* from comments c_1 " +
                    "join comments c_2 on (c_2.id = c_1.comment_id) and c_1.id =? ";
            List<Comment> categories = executeQuery(statement -> {
                statement.setLong(1, comment.getId());
                return statement.executeQuery();
            }, selectCategory, Comment.class);

            comment.setComments(new HashSet<>(categories));
        }
        endTx();

        return comments;
    }

    @Override
    public void save(Comment comment) {
        beginTx();

        String insertProduct = "INSERT INTO comments(id, message, user_id, comment_id, product_id)  value (?, ?, ?, ?, ?)";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, comment.getId());
            preparedStatement.setString(2, comment.getMessage());
            preparedStatement.setLong(3, comment.getCommentId());
            preparedStatement.setLong(4, comment.getProductId());
            preparedStatement.setLong(5, comment.getUserId());
            preparedStatement.executeUpdate();
        }, insertProduct);

        checkComments(comment);

        endTx();
    }

    @Override
    public void update(Comment comment) {
        String sql = "UPDATE comments set message =?, user_id=?, comment_id=?, product_id=? where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(5, comment.getId());
            preparedStatement.setString(1, comment.getMessage());
            preparedStatement.setLong(2, comment.getCommentId());
            preparedStatement.setLong(3, comment.getProductId());
            preparedStatement.setLong(4, comment.getUserId());
            preparedStatement.executeUpdate();
        }, sql);

        checkComments(comment);
    }

    @Override
    public void delete(Comment comment) {
        String sql = "DELETE from comments where id = ?";

        executeQuery(preparedStatement -> {
            preparedStatement.setLong(1, comment.getId());
            preparedStatement.executeUpdate();
        }, sql);
    }

    private void checkComments(Comment comment){
        CommentDao commentDao = new CommentDao(getConnectionFactory());

        for(Comment subComment : comment.getComments()) {
            commentDao.get(subComment.getId()).ifPresentOrElse(null,
                    () -> commentDao.save(subComment));
        }
    }
}
