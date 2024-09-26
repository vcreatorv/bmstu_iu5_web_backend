package com.valer.rip.lab1.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.ConnectionRequest;

@Repository
public class ConnectionRequestRepository {

    private final SessionFactory sessionFactory;
    private final JdbcTemplate jdbcTemplate;

    public ConnectionRequestRepository(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
        this.sessionFactory = sessionFactory;
        this.jdbcTemplate = jdbcTemplate;
    }

    public ConnectionRequest createConnectionRequest() {
        Session session = sessionFactory.getCurrentSession();
        ConnectionRequest connectionRequest = new ConnectionRequest();
        session.persist(connectionRequest);
        return connectionRequest;
    }

    public ConnectionRequest findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(ConnectionRequest.class, id);
    }

    public List<ConnectionRequest> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM ConnectionRequest", ConnectionRequest.class).list();
    }

    public void update(ConnectionRequest connectionRequest) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(connectionRequest);
    }

    public void save(ConnectionRequest connectionRequest) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(connectionRequest);
    }

    public void updateStatusToDeleted(Long requestId) {
        String sql = "UPDATE connection_requests SET status = 'DELETED' WHERE id = ?";
        jdbcTemplate.update(sql, requestId);
    }

    public ConnectionRequest getDraftConnectionRequestByUser(String login) {
        String hql = "FROM ConnectionRequest cr WHERE cr.client.login = :login AND cr.status = 'DRAFT'";
        return sessionFactory.getCurrentSession()
                             .createQuery(hql, ConnectionRequest.class)
                             .setParameter("login", login)
                             .uniqueResultOptional()
                             .orElse(null);
    }
}
