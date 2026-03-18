package luizdeveloper.fileuploader.service;

import io.netty.util.internal.StringUtil;
import jakarta.transaction.Transactional;
import luizdeveloper.fileuploader.dto.DocumentUploadResponse;
import luizdeveloper.fileuploader.enums.DocumentStatus;
import luizdeveloper.fileuploader.infrastructure.exception.EmptyFileException;
import luizdeveloper.fileuploader.infrastructure.storage.AzureBlobStorageWrapper;
import luizdeveloper.fileuploader.mapper.DocumentMapper;
import luizdeveloper.fileuploader.model.Document;
import luizdeveloper.fileuploader.repository.DocumentRepository;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final AzureBlobStorageWrapper azureBlobStorageWrapper;

    public DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper, AzureBlobStorageWrapper azureBlobStorageWrapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.azureBlobStorageWrapper = azureBlobStorageWrapper;
    }
    @Transactional
    public DocumentUploadResponse upload(MultipartFile file) {
        try {

            if (file.isEmpty() || file.getSize() == 0) {
                throw new EmptyFileException("File is empty, failed to upload");
            }
            UUID documentId = UUID.randomUUID();
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isBlank()) {
                throw new RuntimeException("Invalid filename");
            }
            String fileName = StringUtils.cleanPath(originalFileName);
            String extension = StringUtils.getFilenameExtension(fileName);

            String blobName = documentId.toString() + "." +  extension;

            String blobUrl = azureBlobStorageWrapper.upload(blobName, file.getInputStream(),
                    file.getSize());
            log.info("Uploaded file successfully to azure blob storage with url: {}", blobUrl);
            Document document = new Document();
            document.setId(documentId);
            document.setFileName(originalFileName);
            document.setBlobName(blobName);
            document.setContentType(file.getContentType());
            document.setStatus(DocumentStatus.UPLOADED);
            document.setSize(file.getSize());
            document.setUserId("1");

            Document savedDocument = documentRepository.save(document);
            return documentMapper.toDto(savedDocument);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file bytes", e);
        }
    }

}
