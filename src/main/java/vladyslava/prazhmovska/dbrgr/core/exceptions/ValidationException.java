package vladyslava.prazhmovska.dbrgr.core.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import vladyslava.prazhmovska.dbrgr.core.DTOAware;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class ValidationException
        extends RuntimeException
        implements DTOAware<ValidationException.ValidationExceptionDto> {
    private final String code;
    private Map<String, String> invalidFieldValue = new HashMap<>();

    @Override
    public ValidationExceptionDto getDto() {
        return new ValidationExceptionDto(this);
    }

    public static ValidationException of(final String code) {
        return new ValidationException(code);
    }

    public ValidationException invalidFieldValue(final String field, final String value) {
        invalidFieldValue.put(field, value);
        return this;
    }

    @Data
    public static class ValidationExceptionDto {
        private String code;
        private Map<String, String> invalidFieldValue;

        public ValidationExceptionDto(ValidationException e) {
            this.code = e.getCode();
            this.invalidFieldValue = e.getInvalidFieldValue();
        }
    }
}
