package luizdeveloper.fileuploader.dto;

import java.time.Instant;
import java.util.UUID;

public record DocumentUploadResponse(
        UUID id,
        String fileName,
        Long size,
        Instant createdAt
) {
}
