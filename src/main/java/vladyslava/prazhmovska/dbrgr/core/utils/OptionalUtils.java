package vladyslava.prazhmovska.dbrgr.core.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class OptionalUtils {

    public static boolean atLeastOnePresent(Object... objects) {
        return Arrays.stream(objects).anyMatch(Objects::nonNull);
    }
}
