package de.varilx.database.sql;

import de.varilx.configuration.VaxConfiguration;
import de.varilx.database.repository.Repository;
import de.varilx.database.Service;
import de.varilx.database.sql.configuration.HibernateConfiguration;
import de.varilx.database.sql.repository.SQLRepository;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQLService extends Service {

    SessionFactory sessionFactory;

    public SQLService(VaxConfiguration configuration, ClassLoader loader, ServiceType type) {
        super(configuration, loader, type);
        HibernateConfiguration hibernateConfiguration = new HibernateConfiguration(
                configuration.getString("SQL.connection-string"),
                configuration.getString("SQL.username"),
                configuration.getString("SQL.password")
        );

        Configuration hibernateConfig = hibernateConfiguration.toHibernateConfig(loader, type);

        if (type == ServiceType.MYSQL) {
            hibernateConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            hibernateConfig.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        } else if (type == ServiceType.SQLITE) {
            hibernateConfig.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
            hibernateConfig.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
        }

        hibernateConfig.setProperty("hibernate.hbm2ddl.auto", "update");

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("de.varilx", loader))
                .setScanners(new TypeAnnotationsScanner())
        );

        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

        for (Class<?> entityClass : entityClasses) {
            hibernateConfig.addAnnotatedClass(entityClass);
        }

        sessionFactory = hibernateConfig.buildSessionFactory();
    }

    @Override
    public <ENTITY, ID> Repository<ENTITY, ID> createRepositoryInternal(Class<ENTITY> entityClazz, Class<ID> idClass) {
        return new SQLRepository<>(sessionFactory, entityClazz, idClass);
    }
}
