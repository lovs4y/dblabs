package vladyslava.prazhmovska.dbrgr.core.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import com.google.common.base.CaseFormat;

public class EntityMapper {

    @SneakyThrows
    public static <T> T map(Map<String, Object> queryResult, Class<T> mappingType) {
        if (queryResult.isEmpty()) {
            return null;
        }

        T resultEntity = mappingType.getDeclaredConstructor().newInstance();

        Arrays.stream(mappingType.getDeclaredFields()).forEach(field -> {
            Object entityValue = queryResult.get(getFieldName(field));

            field.setAccessible(true);
            Exceptions.rethrow(() -> field.set(resultEntity, entityValue));
        });

        return resultEntity;
    }

    private static String getFieldName(Field field) {
        if (field.getType().isAssignableFrom(Boolean.class) || field.getType().isAssignableFrom(boolean.class)) {
            return "is_" + CaseFormat.LOWER_CAMEL.to(
                    CaseFormat.LOWER_UNDERSCORE,
                    field.getName());
        }

        return CaseFormat.LOWER_CAMEL.to(
                CaseFormat.LOWER_UNDERSCORE,
                field.getName());
    }
}
