package de.varilx.database.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.varilx.database.Repository;
import de.varilx.database.Service;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Objects;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MongoService extends Service {

    MongoClient client;
    MongoDatabase database;

    public MongoService(YamlConfiguration configuration) {
        super(configuration);
        this.client = MongoClients.create(Objects.requireNonNull(configuration.getString("connection-string"), "No Connection String given"));
        this.database = client.getDatabase(Objects.requireNonNull(configuration.getString("database"), "No database given"));
    }

    @Override
    public <ENTITY, ID> Repository<ENTITY, ID> create(String name, Class<ENTITY> entityClazz, Class<ID> idClass) {
        return new MongoRepo<>(database.getCollection(name).withDocumentClass(entityClazz), entityClazz, idClass);
    }
}
