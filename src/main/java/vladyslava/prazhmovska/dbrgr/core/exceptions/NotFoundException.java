package vladyslava.prazhmovska.dbrgr.core.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vladyslava.prazhmovska.dbrgr.core.DTOAware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundException extends RuntimeException implements DTOAware<NotFoundException.NotFoundDetails> {
    private Class<?> entity;
    // This array will be converter to map,
    // so as the first param goes key, the second is value
    private Object[] params;
    public NotFoundException(Class<?> entity, Object... params) {
        this.entity = entity;
        this.params = params;
    }

    @Override
    public String getMessage() {
        return String.format("Entity not found: %s. Params: %s", entity.getName(), Arrays.toString(params));
    }

    public NotFoundDetails getDto() {
        return new NotFoundDetails(this);
    }

    @Data
    public static class NotFoundDetails {
        private Class<?> entity;
        private Map<String, String> params = new HashMap<>();

        public NotFoundDetails(NotFoundException e) {
            this.entity = e.getEntity();
            String key = null;
            for (int i = 0; i < e.params.length; i++) {
                if (i % 2 == 0) {
                    key = e.params[i].toString();
                } else {
                    this.params.put(key, e.params[i].toString());
                }
            }
        }

        public String getEntity() {
            return entity.getName();
        }
    }
}

