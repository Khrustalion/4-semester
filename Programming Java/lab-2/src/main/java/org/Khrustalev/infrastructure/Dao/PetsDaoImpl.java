package org.Khrustalev.infrastructure.Dao;

import org.Khrustalev.domain.Entities.Pets.Pet;
import org.Khrustalev.application.contracts.dao.Dao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class PetsDaoImpl implements Dao<Pet> {
    private final SessionFactory sessionFactory;

    public PetsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Pet save(Pet entity) {
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
            Transaction transaction = session.beginTransaction();
            Pet pet = session.find(Pet.class, id);

            if (pet != null) {
                session.remove(pet);
            }
        }
    }

    @Override
    public void deleteByEntity(Pet entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Pet").executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public Pet update(Pet entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Pet pet = session.merge(entity);
            transaction.commit();

            return pet;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public Pet getById(long id) {
        Session session = sessionFactory.openSession();
        return session.find(Pet.class, id);
    }

    @Override
    public List<Pet> getAll() {
        Session session = sessionFactory.openSession();
        return session.createQuery("SELECT e FROM Pet e", Pet.class).getResultList();
    }
}
