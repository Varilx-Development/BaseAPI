package de.varilx.database.sql.repository;


import de.varilx.database.repository.Repository;
import jakarta.persistence.EntityTransaction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQLRepository<E, ID> implements Repository<E, ID> {

    SessionFactory sessionFactory;
    Class<E> entityClass;
    Class<ID> idClass;

    public SQLRepository(SessionFactory sessionFactory, Class<E> entityClass, Class<ID> idClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
        this.idClass = idClass;
    }


    @Override
    public CompletableFuture<List<E>> sortAll(String field, boolean ascending, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                String query = "FROM " + entityClass.getSimpleName() + " e ORDER BY e." + field + " " + (ascending ? "ASC" : "DESC");
                List<E> results = session.createQuery(query, entityClass).setMaxResults(limit).list();
                session.getTransaction().commit();
                return results;
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<List<E>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                String query = "FROM " + entityClass.getSimpleName();
                List<E> results = session.createQuery(query, entityClass).list();
                session.getTransaction().commit();
                return results;
            }
        });
    }

    @Override
    public CompletableFuture<E> findFirstById(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                E e = session.get(entityClass, id);
                session.getTransaction().commit();
                return e;
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteById(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                E e = session.get(entityClass, id);
                session.remove(e);
                session.getTransaction().commit();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> save(E e) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                session.merge(e);
                session.getTransaction().commit();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> insert(E e) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                EntityTransaction transaction = session.getTransaction();
                transaction.begin();
                session.persist(e);
                transaction.commit();
            }
            return null;
        });
    }

    @Override
    public CompletableFuture<Boolean> exists(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                String query = "SELECT COUNT(1) FROM " + this.entityClass.getSimpleName() + " o WHERE o.id = :id";
                Long result = session.createQuery(query, Long.class).setParameter("id", id).uniqueResult();
                session.getTransaction().commit();
                return result != null && result > 0;
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                String query = "DELETE FROM " + this.entityClass.getSimpleName();
                session.createMutationQuery(query).executeUpdate();
                session.getTransaction().commit();
                return null;
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<E> findByFieldName(String name, Object value) {
        return this.findByFieldNames(Map.of(name, value));
    }

    @Override
    public CompletableFuture<E> findByFieldNames(Map<String, Object> entries) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                session.beginTransaction();
                String hql = "FROM " + entityClass.getName() + (entries.size() > 0 ? " WHERE " : "");

                hql += entries.entrySet().stream().map(entry -> {
                    return entry.getKey() + " = :" +entry.getKey();
                }).collect(Collectors.joining(" AND "));


                Query<E> query = session.createQuery(hql, entityClass);

                for (int i = 0; i < entries.size(); i++) {
                    query.setParameter(entries.keySet().stream().toList().get(i), entries.values().stream().toList().get(i));
                }

                E result = query
                        .uniqueResult();
                session.getTransaction().commit();
                return result;
            }
        });
    }
}
