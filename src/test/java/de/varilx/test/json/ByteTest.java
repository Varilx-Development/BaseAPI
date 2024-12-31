package de.varilx.test.json;

import de.varilx.database.Service;
import de.varilx.database.repository.Repository;
import de.varilx.database.sql.SQLService;
import org.bson.types.Binary;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ByteTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new StringReader("""
                type: Mongo
                Mongo:
                    connection-string: "mongodb://localhost:27017/testdb"
                    database: testdb
                """));

        Service service = Service.load(configuration, this.getClass().getClassLoader());
        Repository<BinaryTestEntity, UUID> repo = service.create(BinaryTestEntity.class, UUID.class);
        repo.deleteAll().get();
        BinaryTestEntity entity = new BinaryTestEntity(UUID.randomUUID(), 15, new Binary(new byte[]{2, 3, 2, 2, 1}));

        repo.insert(entity).get();
        Assertions.assertEquals(repo.findAll().get().getFirst().getData().getData().length, 5);
    }
}
