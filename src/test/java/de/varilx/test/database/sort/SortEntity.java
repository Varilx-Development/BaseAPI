package de.varilx.test.database.sort;

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
public class SortEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    int age;

    public SortEntity(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SortEntity entity)) return false;
        return entity.getId() == this.id && entity.getAge() == this.age;
    }
}
