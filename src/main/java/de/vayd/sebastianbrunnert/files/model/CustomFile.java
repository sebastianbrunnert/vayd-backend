package de.vayd.sebastianbrunnert.files.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.Vayd;
import de.vayd.sebastianbrunnert.discipline.controller.CreateDisciplineController;
import de.vayd.sebastianbrunnert.discipline.controller.DisciplinesController;
import de.vayd.sebastianbrunnert.profile.controller.EditProfileController;
import de.vayd.sebastianbrunnert.profile.controller.ProfileController;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;

@Data
@Entity(name = "files")
@Accessors(chain = true)
public class CustomFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView({DisciplinesController.class, CreateDisciplineController.class, EditProfileController.class, ProfileController.class})
    private String name;

    private Long size;

    private String link;

    @Transient
    @JsonView({DisciplinesController.class, CreateDisciplineController.class, ProfileController.class, EditProfileController.class})
    public String getToken() {
        JwtBuilder builder = Jwts.builder().setAudience("FILE").setSubject(this.id.toString());
        return builder.signWith(SignatureAlgorithm.HS512, Vayd.getInstance().getKey()).compact();
    }

    @PreRemove
    public void deleteFile() {
        Vayd.getInstance().getAccessComponent().getStorageService().deleteResource(this.link);
    }

}
