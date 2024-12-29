package de.varilx.database.sql.repository;


import de.varilx.database.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQLRepository<E, ID> implements Repository<E, ID> {

    SessionFactory sessionFactory;
    Class<E> entityClass;
    Class<ID> idClass;
    String name;

    public SQLRepository(SessionFactory sessionFactory, Class<E> entityClass, Class<ID> idClass, String name) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
        this.idClass = idClass;
        this.name = name;
    }


    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<List<E>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            String query = "FROM " + entityClass.getSimpleName();
            List<E> results = session.createQuery(query, entityClass).list();
            session.getTransaction().commit();
            session.close();
            return results;
        });
    }

    @Override
    public CompletableFuture<E> findFirstById(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            E entity = session.get(entityClass, id);
            session.getTransaction().commit();
            session.close();
            return entity;
        });
    }

    @Override
    public CompletableFuture<Void> deleteById(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            E e = session.get(entityClass, id);
            session.remove(e);
            session.getTransaction().commit();
            session.close();
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> save(E e) {
        return CompletableFuture.supplyAsync(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            session.persist(e);
            session.getTransaction().commit();
            session.close();
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> insert(E e) {
        return CompletableFuture.supplyAsync(() -> {
            Session session = this.sessionFactory.openSession();
            EntityTransaction transaction = session.getTransaction();
            transaction.begin();
            session.save(e);
            transaction.commit();
            session.close();
            return null;
        });
    }

    @Override
    public CompletableFuture<Boolean> exists(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            String query = "SELECT COUNT(1) FROM " + name + " o WHERE o.id = :id";
            E result = session.createQuery(query, entityClass).setParameter("id", id.toString())
                    .uniqueResult();
            session.getTransaction().commit();
            session.close();
            return result != null;
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.supplyAsync(() -> {
            Session session = this.sessionFactory.openSession();
            session.beginTransaction();
            String query = "DELETE FROM " + name;
            session.createQuery(query, entityClass).executeUpdate();
            session.getTransaction().commit();
            session.close();
            return null;
        });
    }
}
