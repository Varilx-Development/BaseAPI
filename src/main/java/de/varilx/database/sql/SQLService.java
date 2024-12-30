package de.varilx.database.sql;

import de.varilx.database.repository.Repository;
import de.varilx.database.Service;
import de.varilx.database.sql.configuration.HibernateConfiguration;
import de.varilx.database.sql.repository.SQLRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.YamlConfiguration;
import org.hibernate.SessionFactory;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQLService extends Service {

    SessionFactory sessionFactory;

    public SQLService(YamlConfiguration configuration, ClassLoader loader, ServiceType type) {
        super(configuration, loader, type);
        HibernateConfiguration hibernateConfiguration = new HibernateConfiguration(
                configuration.getString("SQL.connection-string"),
                configuration.getString("SQL.username"),
                configuration.getString("SQL.password")
        );


        sessionFactory = hibernateConfiguration.toHibernateConfig(loader, type).buildSessionFactory();
    }

    @Override
    public <ENTITY, ID> Repository<ENTITY, ID> createRepositoryInternal(Class<ENTITY> entityClazz, Class<ID> idClass) {
        return new SQLRepository<>(sessionFactory, entityClazz, idClass);
    }
}
