package luizdeveloper.fileuploader.infrastructure.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.specialized.BlockBlobClient;
import luizdeveloper.fileuploader.infrastructure.exception.StorageIntegrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;

@Component
public class AzureBlobStorageWrapper {

    private final String containerName;
    private final BlobServiceClient blobServiceClient;

    public AzureBlobStorageWrapper(@Value("${STORAGE_CONTAINER_NAME}") String containerName,
                                   @Value("${STORAGE_CONNECTION_STRING}") String connectionString) {
        this.containerName = containerName;
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    public String upload(String blobName, InputStream content, long size) {

        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            BlockBlobClient blockBlobClient = containerClient.getBlobClient(blobName).getBlockBlobClient();

            blockBlobClient.upload(new BufferedInputStream(content), size);

            return blockBlobClient.getBlobUrl();
        } catch (BlobStorageException ex) {
            throw new StorageIntegrationException("Failed to communicate with cloud storage provider", ex);
        }


    }

    public InputStream download(String blobName) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            return blobClient.openInputStream();
        } catch (BlobStorageException ex) {
            throw new StorageIntegrationException("Failed to retrieve the file in the cloud storage", ex);
        }
    }

    public void delete(String blobName) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
            blobClient.deleteIfExists();

        } catch (BlobStorageException e) {
            throw new StorageIntegrationException("Failed to delete the orphaned file in the cloud storage");
        }
    }
}
