package de.varilx.test.list;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SchoolClass {

    @Id
    @de.varilx.database.id.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;


    @ManyToOne
    TestEntity user;

    public SchoolClass(TestEntity user) {
        this.user = user;
    }
}
