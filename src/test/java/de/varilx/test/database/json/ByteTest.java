package de.varilx.test.database.json;

import de.varilx.config.Configuration;
import de.varilx.database.Service;
import de.varilx.database.repository.Repository;
import org.bson.types.Binary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ByteTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Configuration configuration = new Configuration("""
                type: SQLITE
                SQL:
                    connection-string: "jdbc:sqlite:sample.db"
                """);

        Service service = Service.load(configuration, this.getClass().getClassLoader());
        Repository<BinaryTestEntity, UUID> repo = service.create(BinaryTestEntity.class, UUID.class);
        repo.deleteAll().get();
        BinaryTestEntity entity = new BinaryTestEntity(UUID.randomUUID(), 15, new Binary(new byte[]{2, 3, 2, 2, 1}));

        repo.insert(entity).get();
        Assertions.assertEquals(repo.findAll().get().getFirst().getData().getData().length, 5);
    }
}
