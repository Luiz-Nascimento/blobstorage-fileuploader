package luizdeveloper.fileuploader.infrastructure.exception;

import java.time.Instant;

public record ApiErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
