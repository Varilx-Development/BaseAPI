package de.varilx.test.json;

import de.varilx.database.id.MongoId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.Binary;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BinaryTestEntity {

    @MongoId
    @Id
    UUID id;

    int age;

    Binary data;

    public BinaryTestEntity(int age, Binary data, UUID id) {
        this.age = age;
        this.data = data;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BinaryTestEntity entity)) return false;
        return entity.getId() == this.id && entity.getAge() == this.age;
    }
}
