package de.vayd.sebastianbrunnert.discipline.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity(name = "disciplines")
@Data
@Accessors(chain = true)
public class Discipline {

    @Id
    private Long id;

    private String name;

}
