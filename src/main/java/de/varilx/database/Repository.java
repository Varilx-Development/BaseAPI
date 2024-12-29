package de.varilx.database;

import java.util.List;

public interface Repository<ENTITY, ID> {

    List<ENTITY> findAll();

    ENTITY findFirstById(ID id);

    void deleteById(ID id);

    void save(ENTITY entity);

    boolean exists(ID id);

    void deleteAll();

}
