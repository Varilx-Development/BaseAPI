package de.varilx.database.sql.configuration;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HibernateConfiguration {

    String connectionUri, userName, password;

    public Configuration toHibernateConfig(ClassLoader loader) {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", connectionUri);
        configuration.setProperty("hibernate.connection.username", userName);
        configuration.setProperty("hibernate.connection.password", password);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("spring.jpa.hibernate.ddl-auto", "auto");

        Logger reflectionsLogger = Logger.getLogger("org.reflections");
        reflectionsLogger.setLevel(Level.OFF);

        for (Package definedPackage : loader.getDefinedPackages()) {
            Reflections reflections = new Reflections(definedPackage.getName());
            for (Class<?> aClass : reflections.getTypesAnnotatedWith(Entity.class)) {
                configuration.addAnnotatedClass(aClass);
            }
        }



        return configuration;
    }

}
