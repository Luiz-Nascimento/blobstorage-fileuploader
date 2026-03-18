package luizdeveloper.fileuploader.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StorageIntegrationException.class)
    public ResponseEntity<ApiErrorResponse> handleStorageIntegrationException(
            StorageIntegrationException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_GATEWAY;

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                "External Integration Error",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                "Internal Server Error",
                "An unexpected error has occurred, try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
