package de.vayd.sebastianbrunnert.discipline.model;

import com.fasterxml.jackson.annotation.JsonView;
import de.vayd.sebastianbrunnert.discipline.controller.DisciplinesController;
import de.vayd.sebastianbrunnert.files.model.CustomFile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity(name = "disciplines")
@Data
@Accessors(chain = true)
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(DisciplinesController.class)
    private Long id;

    @JsonView(DisciplinesController.class)
    private String name;

    @OneToOne
    @JsonView(DisciplinesController.class)
    private CustomFile cover;

    @JsonView(DisciplinesController.class)
    private String metric;

    @JsonView(DisciplinesController.class)
    private MetricType metricType;

}
