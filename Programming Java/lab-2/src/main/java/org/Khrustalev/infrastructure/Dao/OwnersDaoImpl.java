package org.Khrustalev.infrastructure.Dao;

import org.Khrustalev.domain.Entities.Owners.Owner;
import org.Khrustalev.application.contracts.dao.Dao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class OwnersDaoImpl implements Dao<Owner> {
    private final SessionFactory sessionFactory;
    
    public OwnersDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Owner save(Owner entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();

            return entity;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction =  session.beginTransaction();
            Owner Owner = session.find(Owner.class, id);

            if (Owner != null) {
                session.remove(Owner);
            }

            transaction.commit();
        }
    }

    @Override
    public void deleteByEntity(Owner entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction =  session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction =  session.beginTransaction();
            session.createQuery("DELETE FROM Owner").executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public Owner update(Owner entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction =  session.beginTransaction();
            Owner owner = session.merge(entity);

            transaction.commit();

            return owner;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public Owner getById(long id) {
        Session session = sessionFactory.openSession();

        return session.find(Owner.class, id);
    }

    @Override
    public List<Owner> getAll() {
        Session session = sessionFactory.openSession();

        return session.createQuery("SELECT e FROM Owner e", Owner.class).getResultList();
    }
}
