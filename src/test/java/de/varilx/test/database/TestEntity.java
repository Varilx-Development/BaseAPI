package de.varilx.test.database;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    int age;

    public TestEntity(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestEntity entity)) return false;
        return entity.getId() == this.id && entity.getAge() == this.age;
    }
}
