package de.vayd.sebastianbrunnert.files.services;

import de.vayd.sebastianbrunnert.files.exceptions.FileException;
import de.vayd.sebastianbrunnert.files.model.CustomFile;
import de.vayd.sebastianbrunnert.files.repository.CustomFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class StorageService {

    @Autowired
    private CustomFileRepository customFileRepository;

    public StorageService() {

    }

    public CustomFile uploadResource(MultipartFile multipartFile) throws FileException {
        throw new FileException();
    }

    public InputStream getResource(String link) throws FileException {
        throw new FileException();
    }

    public boolean existsResource(String link) {
        return false;
    }

    public void deleteResource(String link) {

    }

}
