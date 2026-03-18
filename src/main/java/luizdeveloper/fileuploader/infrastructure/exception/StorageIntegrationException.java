package luizdeveloper.fileuploader.infrastructure.exception;

public class StorageIntegrationException extends RuntimeException {

    public StorageIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
    public StorageIntegrationException(String message) {
        super(message);
    }
}
