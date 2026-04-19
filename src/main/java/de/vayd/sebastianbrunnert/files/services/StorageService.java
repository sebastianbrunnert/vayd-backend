package de.vayd.sebastianbrunnert.files.services;

import de.vayd.sebastianbrunnert.files.exceptions.FileException;
import de.vayd.sebastianbrunnert.files.model.CustomFile;
import de.vayd.sebastianbrunnert.files.repository.CustomFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class StorageService {

    @Autowired
    private CustomFileRepository customFileRepository;

    public CustomFile uploadResource(InputStream inputStream, String name) throws FileException {
        CustomFile customFile = new CustomFile().setName(name);

        String link = UUID.randomUUID().toString() + "-" + name;
        File storageFile = new File("storage/" + link);
        storageFile.getParentFile().mkdirs();
        try (var outputStream = Files.newOutputStream(storageFile.toPath())) {
            inputStream.transferTo(outputStream);
        } catch (Exception e) {
            throw new FileException();
        }

        customFile.setLink(link);
        return this.customFileRepository.save(customFile);
    }

    public InputStream getResource(String link) throws FileException {
        try {
            return new FileInputStream("storage/" + link);
        } catch (Exception e) {
            throw new FileException();
        }
    }

    public boolean existsResource(String link) {
        return new File("storage/" + link).exists();
    }

    public void deleteResource(String link) {
        File file = new File("storage/" + link);
        if (file.exists()) {
            file.delete();
        }
    }

}
