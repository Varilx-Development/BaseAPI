package de.varilx.test.sort;

import de.varilx.database.Service;
import de.varilx.database.repository.Repository;
import de.varilx.database.sql.SQLService;
import de.varilx.test.TestEntity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SortTest {

    @BeforeEach
    public void deleteBefore() {
        new File("sample.db").delete();
    }

    @AfterEach
    public void deleteAfter() {
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
        Repository<SortEntity, UUID> repo = service.create(SortEntity.class, UUID.class);

        repo.deleteAll().get();
        repo.insert(new SortEntity(5)).get();
        repo.insert(new SortEntity(2)).get();
        repo.insert(new SortEntity(10)).get();

        List<SortEntity> result = repo.sortAll("age", false, 2).get();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(10, result.getFirst().getAge());
        Assertions.assertEquals(5, result.get(1).getAge());

    }

}
