package com.valer.rip.lab1.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.User;

@Repository
public class UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
    }

    public User findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM User", User.class).list();
    }
}
