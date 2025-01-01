package de.varilx.test.database.list;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestListEntity {

    @Id
    UUID id;

    int age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<SchoolClass> classes;

    public TestListEntity(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TestListEntity entity)) return false;
        return entity.getId() == this.id && entity.getAge() == this.age;
    }
}
