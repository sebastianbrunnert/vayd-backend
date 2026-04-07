package de.vayd.sebastianbrunnert.files.model;

import de.vayd.sebastianbrunnert.Vayd;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity(name = "files")
@Accessors(chain = true)
public class CustomFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long size;

    private String link;

    @Transient
    public String getToken() {
        JwtBuilder builder = Jwts.builder().setAudience("FILE").setSubject(this.id.toString());
        return builder.signWith(SignatureAlgorithm.HS512, Vayd.getInstance().getKey()).compact();
    }

    @PreRemove
    public void deleteFile() {
        Vayd.getInstance().getAccessComponent().getStorageService().deleteResource(this.link);
    }

}
