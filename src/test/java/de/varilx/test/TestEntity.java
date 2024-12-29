package de.varilx.test;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class TestEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int age;

    public TestEntity(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestEntity entity)) return false;
        return entity.getId() == this.id && entity.getAge() == this.age;
    }
}
