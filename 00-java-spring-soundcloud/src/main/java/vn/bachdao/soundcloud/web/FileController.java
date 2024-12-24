package vn.bachdao.soundcloud.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.bachdao.soundcloud.service.FileService;
import vn.bachdao.soundcloud.service.dto.repsonse.file.ResUploadFileDTO;
import vn.bachdao.soundcloud.service.util.annotation.ApiMessage;
import vn.bachdao.soundcloud.web.rest.errors.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;

    @Value("${jhipster.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> handleFileUpload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder)
            throws URISyntaxException, IOException, StorageException {

        // file empty validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a file");
        }

        // file extension
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "mp3", "wav");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException("Invalid file extention. Only allows " + allowedExtensions.toString());
        }

        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);

        // store file
        String uploadFile = this.fileService.store(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok(res);
    }
}
