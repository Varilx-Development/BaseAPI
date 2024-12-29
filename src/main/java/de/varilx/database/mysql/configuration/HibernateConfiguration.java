package de.varilx.database.mysql.configuration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HibernateConfiguration {

    String connectionUri, userName, password;

    public Configuration toHibernateConfig() {
        Configuration configuration = new Configuration();

        
    }

}
