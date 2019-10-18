package com.hibernate.dao;


import com.jdbc.model.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CommentDao {
    public void save(Comment comment) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(comment);
        session.getTransaction().commit();
        session.close();

    }
}
