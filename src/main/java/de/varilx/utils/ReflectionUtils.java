package de.varilx.utils;

import de.varilx.database.id.MongoId;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public <ID> ID getId(Object object, Class<ID> idClass) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (!field.getType().equals(idClass)) continue;
            if (field.isAnnotationPresent(MongoId.class)) {
                try {
                    return (ID) field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }


}
