package com.valer.rip.lab1.repositories;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.ProviderDuty;

@Repository
public class ProviderDutyRepository {
    
    private final SessionFactory sessionFactory;
   
    public ProviderDutyRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ProviderDuty findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(ProviderDuty.class, id);
    }

    public List<ProviderDuty> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM ProviderDuty", ProviderDuty.class).list();
    }

    public List<ProviderDuty> findDutyByTitle(String title) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM ProviderDuty pd WHERE LOWER(pd.title) LIKE :title";
        return session.createQuery(hql, ProviderDuty.class)
                      .setParameter("title", "%" + title + "%")
                      .list();
    }
}
