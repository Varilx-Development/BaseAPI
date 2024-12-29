package de.varilx.test;


import de.varilx.database.Repository;
import de.varilx.database.sql.SQLService;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SQLiteTest {

    @Test
    public void testSQL() {
        SQLService service = new SQLService("jdbc:mysql://127.0.0.1:3306/db", "user", "password", this.getClass().getClassLoader());
        Repository<TestEntity, UUID> repo = service.create("TestEntity", TestEntity.class, UUID.class);
        try {
            UUID uuid = UUID.randomUUID();
            repo.insert(new TestEntity(uuid, 20)).get();
            System.out.println(repo.findFirstById(uuid).get());
            System.out.println(repo.findAll().get().size());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }



}
