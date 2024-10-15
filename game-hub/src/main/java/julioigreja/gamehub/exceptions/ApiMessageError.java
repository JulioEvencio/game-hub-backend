package julioigreja.gamehub.exceptions;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record ApiMessageError(
        String message,
        List<String> errors,
        Instant instant
) implements Serializable {

    public ApiMessageError(String message, List<String> errors) {
        this(message, errors, Instant.now());
    }

}
