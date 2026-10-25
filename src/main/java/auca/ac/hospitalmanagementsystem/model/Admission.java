package auca.ac.hospitalmanagementsystem.model;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "admission")
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDate admissionDate;

    @Enumerated(EnumType.STRING)
    private AdmissionStatus status;

    private String complaint;

    @ManyToOne
    @JoinColumn(name = "inpatient_id")
    private Inpatient inpatient;

    @ManyToOne
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;

    public Admission() {}

    public Admission(LocalDate admissionDate, AdmissionStatus status, String complaint, Inpatient inpatient, Nurse nurse) {
        this.admissionDate = admissionDate;
        this.status = status;
        this.complaint = complaint;
        this.inpatient = inpatient;
        this.nurse = nurse;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public AdmissionStatus getStatus() {
        return status;
    }

    public void setStatus(AdmissionStatus status) {
        this.status = status;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public Inpatient getInpatient() {
        return inpatient;
    }

    public void setInpatient(Inpatient inpatient) {
        this.inpatient = inpatient;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }
}
