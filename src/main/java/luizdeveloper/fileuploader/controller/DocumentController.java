package luizdeveloper.fileuploader.controller;

import luizdeveloper.fileuploader.dto.DocumentUploadResponse;
import luizdeveloper.fileuploader.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> upload(
            @RequestParam("file") MultipartFile file) {
        DocumentUploadResponse response = documentService.upload(file);
        return ResponseEntity.ok(response);
    }
}
