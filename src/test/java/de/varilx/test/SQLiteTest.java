package de.varilx.test;


import de.varilx.database.Service;
import de.varilx.database.repository.Repository;
import de.varilx.database.sql.SQLService;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SQLiteTest {

    @BeforeEach
    public void delete() {
        new File("sample.db").delete();
    }

    @Test
    public void testSQL() throws ExecutionException, InterruptedException {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new StringReader("""
                type: SQLITE
                SQL:
                    connection-string: "jdbc:sqlite:sample.db"
                """));

        SQLService service = (SQLService) Service.load(configuration, this.getClass().getClassLoader());
        Repository<TestEntity, UUID> repo = service.create(TestEntity.class, UUID.class);

        repo.deleteAll().get();
        Assertions.assertEquals(repo.exists(UUID.randomUUID()).get(), false);
        repo.insert(new TestEntity(20)).get();
        List<TestEntity> result = repo.findAll().get();
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(repo.exists(result.getFirst().getId()).get(), true);
        Assertions.assertEquals(repo.findFirstById(result.getFirst().getId()).get(), result.getFirst());
        repo.deleteAll().get();
        Assertions.assertEquals(repo.findAll().get().size(), 0);
        repo.insert(new TestEntity(20)).get();
        result = repo.findAll().get();
        repo.deleteById(result.getFirst().getId()).get();
        Assertions.assertEquals(repo.findAll().get().size(), 0);
    }


}
