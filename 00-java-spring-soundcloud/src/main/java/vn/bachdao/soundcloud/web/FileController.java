package vn.bachdao.soundcloud.web;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.bachdao.soundcloud.service.FileService;

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
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folder)
            throws URISyntaxException, IOException {

        // skip validate

        // create a directory if not exist
        this.fileService.createDirectory(baseURI + folder);

        // store file
        this.fileService.store(file, folder);

        return file.getOriginalFilename() + folder;
    }
}
