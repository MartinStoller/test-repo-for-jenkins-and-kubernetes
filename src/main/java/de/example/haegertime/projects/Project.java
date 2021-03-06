package de.example.haegertime.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.example.haegertime.customer.Customer;
import de.example.haegertime.timetables.TimeTableDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sonatype.inject.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name="projects")
@Validated
@NoArgsConstructor
public class Project {
    @Id
    @SequenceGenerator(
            name="project_sequence",
            sequenceName = "project_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "project_sequence"
    )
    private Long id;
    @NotNull @NotBlank
    private String title;
    @NotNull
    private LocalDate start;
    @Nullable
    private LocalDate end;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<TimeTableDay> timeTableDays;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="pj_fk", referencedColumnName = "id")
    private Customer customer;

    public Project(String title, LocalDate start, LocalDate end){
        this.title = title;
        this.start = start;
        this.end = end;
        this.timeTableDays = new ArrayList<>();
    }

}
