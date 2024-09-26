package com.valer.rip.lab1.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.DutyRequest;

@Repository
public class DutyRequestRepository {

    private final SessionFactory sessionFactory;

    public DutyRequestRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(DutyRequest dutyRequest) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(dutyRequest);
    }

    public DutyRequest findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(DutyRequest.class, id);
    }

    public List<DutyRequest> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM DutyRequest", DutyRequest.class).list();
    }

    public void update(DutyRequest dutyRequest) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(dutyRequest);
    }

    public void delete(DutyRequest dutyRequest) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(dutyRequest);
    }
}
