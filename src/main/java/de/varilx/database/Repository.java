package de.varilx.database;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Repository<ENTITY, ID> {

    CompletableFuture<List<ENTITY>> findAll();

    CompletableFuture<ENTITY> findFirstById(ID id);

    void deleteById(ID id);

    void save(ENTITY entity);

    void insert(ENTITY entity);

    CompletableFuture<Boolean> exists(ID id);

    void deleteAll();

}
