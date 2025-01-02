package de.varilx.database.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import de.varilx.configuration.VaxConfiguration;
import de.varilx.database.repository.Repository;
import de.varilx.database.Service;
import de.varilx.database.mongo.repository.MongoRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;
import java.util.Objects;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MongoService extends Service {

    MongoClient client;
    MongoDatabase database;

    public MongoService(VaxConfiguration configuration, ClassLoader loader, ServiceType type) {
        super(configuration, loader, type);

        CodecRegistry codecRegistry =  fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder()
                        .automatic(true)
                        .conventions(List.of(
                                Conventions.ANNOTATION_CONVENTION,
                                Conventions.SET_PRIVATE_FIELDS_CONVENTION
                        ))
                        .build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(Objects.requireNonNull(configuration.getString("Mongo.connection-string"), "No Connection String given")))
                .codecRegistry(codecRegistry)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();

        this.client = MongoClients.create(settings);
        this.database = client.getDatabase(Objects.requireNonNull(configuration.getString("Mongo.database"), "No database given"));
    }

    @Override
    protected  <ENTITY, ID> Repository<ENTITY, ID> createRepositoryInternal(Class<ENTITY> entityClazz, Class<ID> idClass) {
        if (database.getCollection(entityClazz.getSimpleName()) == null) {
            database.createCollection(entityClazz.getSimpleName());
        }
        return new MongoRepository<>(database.getCollection(entityClazz.getSimpleName()).withDocumentClass(entityClazz), entityClazz, idClass);
    }
}
