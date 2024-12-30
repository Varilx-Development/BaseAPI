package de.varilx.database.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.varilx.database.repository.Repository;
import de.varilx.database.Service;
import de.varilx.database.mongo.repository.MongoRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Objects;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MongoService extends Service {

    MongoClient client;
    MongoDatabase database;

    public MongoService(YamlConfiguration configuration, ClassLoader loader, ServiceType type) {
        super(configuration, loader, type);
        this.client = MongoClients.create(Objects.requireNonNull(configuration.getString("connection-string"), "No Connection String given"));
        this.database = client.getDatabase(Objects.requireNonNull(configuration.getString("database"), "No database given"));
    }

    @Override
    public <ENTITY, ID> Repository<ENTITY, ID> create(Class<ENTITY> entityClazz, Class<ID> idClass) {
        if (database.getCollection(entityClazz.getSimpleName()) == null) {
            database.createCollection(entityClazz.getSimpleName());
        }
        MongoRepository<ENTITY, ID> repo = new MongoRepository<>(database.getCollection(entityClazz.getSimpleName()).withDocumentClass(entityClazz), entityClazz, idClass);
        this.getRepositoryMap().put(entityClazz, repo);
        return repo;
    }
}
