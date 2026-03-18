package luizdeveloper.fileuploader.mapper;

import luizdeveloper.fileuploader.dto.DocumentUploadResponse;
import luizdeveloper.fileuploader.model.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentUploadResponse toDto(Document document);
}
