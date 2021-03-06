package de.example.haegertime.customer;

import de.example.haegertime.projects.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @SequenceGenerator(
            name="customer_sequence",
            sequenceName = "customer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_sequence"
    )
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;


    @OneToMany(targetEntity = Project.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name="pj_fk", referencedColumnName = "id")
    private List<Project> projects;

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
