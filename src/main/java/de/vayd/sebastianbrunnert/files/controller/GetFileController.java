package de.vayd.sebastianbrunnert.files.controller;

import com.amazonaws.util.IOUtils;
import de.vayd.sebastianbrunnert.authentication.model.intern.CustomFileAuthentication;
import de.vayd.sebastianbrunnert.files.exceptions.FileException;
import de.vayd.sebastianbrunnert.files.model.CustomFile;
import de.vayd.sebastianbrunnert.files.repository.CustomFileRepository;
import de.vayd.sebastianbrunnert.files.services.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@PreAuthorize("@access.isFile()")
@RestController
@RequestMapping("file")
public class GetFileController {

    @Autowired
    private CustomFileRepository customFileRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping
    public void getFile(HttpServletResponse response) throws IOException, FileException {
        CustomFileAuthentication authentication = (CustomFileAuthentication) SecurityContextHolder.getContext().getAuthentication();
        CustomFile customFile = authentication.getCustomFile();

        String fileName = customFile.getName();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        if (fileName.endsWith(".pdf")) {
            response.setContentType("application/pdf");
        } else {
            response.setContentType("application/octet-stream");
        }

        // RFC 6266-compliant header: ASCII fallback + UTF-8 encoded name
        response.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"; filename*=UTF-8''%s", fileName.replaceAll("[^\\x20-\\x7E]", "_"), encodedFileName));

        IOUtils.copy(this.storageService.getResource(customFile.getLink()), response.getOutputStream());
        response.flushBuffer();
    }


}
