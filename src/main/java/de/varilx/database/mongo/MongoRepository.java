package de.varilx.database.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import de.varilx.database.Repository;
import de.varilx.utils.ReflectionUtils;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MongoRepository<E, ID> implements Repository<E, ID> {

    private final MongoCollection<E> database;
    private final Class<E> entityClass;
    private final Class<ID> idClass;

    public MongoRepository(MongoCollection<E> database, Class<E> entityClass, Class<ID> idClass) {
        this.database = database;
        this.entityClass = entityClass;
        this.idClass = idClass;
    }


    @Override
    public CompletableFuture<List<E>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            return this.database.find().into(new ArrayList<>());
        });
    }

    @Override
    public CompletableFuture<E> findFirstById(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            return this.database.find(createIdFilter(id)).first();
        });
    }

    @Override
    public CompletableFuture<Void> deleteById(ID id) {
       return CompletableFuture.supplyAsync(() -> {
            this.database.deleteOne(createIdFilter(id));
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> save(E e) {
      return   CompletableFuture.supplyAsync(() -> {
            Bson idFilter = createIdFilter(getId(e));
            if (database.countDocuments(idFilter) > 0) {
                UpdateResult result = database.replaceOne(idFilter, e, new ReplaceOptions().upsert(true));
                return null;
            }
            database.insertOne(e);
          return null;

        });
    }

    @Override
    public CompletableFuture<Void> insert(E e) {
       return CompletableFuture.supplyAsync(() -> {
            database.insertOne(e);
           return null;

        });
    }

    @Override
    public CompletableFuture<Boolean> exists(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            Bson idFilter = createIdFilter(id);
            return database.countDocuments(idFilter) > 0;
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() {
        return CompletableFuture.supplyAsync(() -> {
            database.deleteMany(Filters.exists("_id"));
            return null;
        });
    }

    private ID getId(E entity) {
        return ReflectionUtils.getId(entity, idClass);
    }

    private @NotNull Bson createIdFilter(ID id) {
        return Filters.eq("_id", id);
    }
}
