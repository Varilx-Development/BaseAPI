package de.varilx.test.list;

import de.varilx.database.Service;
import de.varilx.database.repository.Repository;
import de.varilx.database.sql.SQLService;
import jakarta.transaction.Transactional;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ListTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new StringReader("""
                type: SQLITE
                SQL:
                    connection-string: "jdbc:sqlite:sample.db"
                """));

        SQLService service = (SQLService) Service.load(configuration, this.getClass().getClassLoader());
        Repository<TestEntity, UUID> repo = service.create(TestEntity.class, UUID.class);
        repo.deleteAll().get();
        TestEntity entity = new TestEntity(UUID.randomUUID(), 15, new ArrayList<>());
        entity.getClasses().add(new SchoolClass(entity));

        repo.insert(entity).get();
        Assertions.assertEquals(repo.findAll().get().getFirst().getClasses().size(), 1);
    }
}
