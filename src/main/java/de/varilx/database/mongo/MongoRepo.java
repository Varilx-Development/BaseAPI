package de.varilx.database.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import de.varilx.database.Repository;
import de.varilx.utils.ReflectionUtils;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MongoRepo<E, ID> implements Repository<E, ID> {

    private final MongoCollection<E> database;
    private final Class<E> entityClass;
    private final Class<ID> idClass;

    public MongoRepo(MongoCollection<E> database, Class<E> entityClass, Class<ID> idClass) {
        this.database = database;
        this.entityClass = entityClass;
        this.idClass = idClass;
    }


    @Override
    public List<E> findAll() {
        return this.database.find().into(new ArrayList<>());
    }

    @Override
    public E findFirstById(ID id) {
        return this.database.find(createIdFilter(id)).first();
    }

    @Override
    public void deleteById(ID id) {
        this.database.deleteOne(createIdFilter(id));
    }

    @Override
    public void save(E e) {
        Bson idFilter = createIdFilter(getId(e));
        if (database.countDocuments(idFilter) > 0) {
            UpdateResult result = database.replaceOne(idFilter, e, new ReplaceOptions().upsert(true));
        }
        database.insertOne(e);
    }

    @Override
    public boolean exists(ID id) {
        Bson idFilter = createIdFilter(id);
        return database.countDocuments(idFilter) > 0;
    }

    @Override
    public void deleteAll() {
        database.deleteMany(Filters.exists("_id"));
    }

    private ID getId(E entity) {
        return ReflectionUtils.getId(entity, idClass);
    }

    private @NotNull Bson createIdFilter(ID id) {
        return Filters.eq("_id", id);
    }
}
