package auca.ac.hospitalmanagementsystem.model;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "ward")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int wardNumber;
    private String name;

    public Ward() {}

    public Ward(int wardNumber, String name) {
        this.wardNumber = wardNumber;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
