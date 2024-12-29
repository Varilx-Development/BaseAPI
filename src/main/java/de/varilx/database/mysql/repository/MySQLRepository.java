package de.varilx.database.mysql.repository;


import de.varilx.database.Repository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MySQLRepository<E, ID> implements Repository<E, ID> {

    Session session;

    public MySQLRepository(SessionFactory sessionFactory) {
        session = sessionFactory.openSession();
    }

    

    @Override
    public CompletableFuture<List<E>> findAll() {
        return null;
    }

    @Override
    public CompletableFuture<E> findFirstById(ID id) {
        return null;
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void save(E e) {

    }

    @Override
    public void insert(E e) {

    }

    @Override
    public CompletableFuture<Boolean> exists(ID id) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
