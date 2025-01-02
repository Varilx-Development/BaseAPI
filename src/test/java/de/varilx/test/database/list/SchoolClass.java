package de.varilx.test.database.list;

import de.varilx.database.id.MongoId;
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
    @MongoId
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;


    @ManyToOne
    TestListEntity user;

    public SchoolClass(TestListEntity user) {
        this.user = user;
    }
}