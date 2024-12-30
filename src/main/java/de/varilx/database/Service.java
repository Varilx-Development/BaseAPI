package de.varilx.database;

import com.google.common.reflect.ClassPath;
import de.varilx.database.mongo.MongoService;
import de.varilx.database.sql.SQLService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Service {

    @Getter
    private final Map<Class<?>, Repository<?, ?>> repositoryMap = new HashMap<>();

    public Service(YamlConfiguration configuration, ClassLoader loader, ServiceType type) {

    }

    public abstract  <ENTITY, ID> Repository<ENTITY, ID> create(Class<ENTITY> entityClazz, Class<ID> idClass);

    public static Service load(YamlConfiguration configuration, ClassLoader loader) {
        @Nullable ServiceType type = ServiceType.findBy(configuration.getString("type"));
        if (type == null) throw new RuntimeException("No Database Type found");
        try {
            return (Service) type.getClazz().getDeclaredConstructors()[0].newInstance(configuration, loader, type);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    @AllArgsConstructor
    @Getter

    public enum ServiceType {

        MONGO(MongoService.class),
        MYSQL(SQLService.class),
        SQLITE(SQLService.class);


        private final Class<? extends Service> clazz;

        @Nullable
        public static ServiceType findBy(@Nullable String type) {
            if (type == null) return null;
            for (ServiceType value : values()) {
                if (value.name().equalsIgnoreCase(type)) return value;
            }
            return null;
        }

    }
}
