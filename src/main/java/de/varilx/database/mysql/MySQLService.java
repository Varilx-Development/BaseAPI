package de.varilx.database.mysql;

import de.varilx.database.Repository;
import de.varilx.database.Service;
import de.varilx.database.mysql.configuration.HibernateConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.YamlConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Objects;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MySQLService extends Service {

    SessionFactory sessionFactory;

    public MySQLService(YamlConfiguration configuration) {
        super(configuration);
        HibernateConfiguration hibernateConfiguration = new HibernateConfiguration(
                Objects.requireNonNull(configuration.getString("MySQL.connection-string")),
                Objects.requireNonNull(configuration.getString("MySQL.username")),
                Objects.requireNonNull(configuration.getString("MySQL.password"))
        );


        sessionFactory = hibernateConfiguration.toHibernateConfig().buildSessionFactory();
    }

    @Override
    public <ENTITY, ID> Repository<ENTITY, ID> create(String name, Class<ENTITY> entityClazz, Class<ID> idClass) {
        return null;
    }
}
