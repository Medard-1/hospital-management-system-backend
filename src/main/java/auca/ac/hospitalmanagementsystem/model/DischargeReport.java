package auca.ac.hospitalmanagementsystem.model;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "discharge_report")
public class DischargeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String diagnosis;
    private String instructions;
    private int followUpDays;
    private LocalDate issuedDate;

    @ManyToOne
    @JoinColumn(name = "admission_id")
    private Admission admission;

    public DischargeReport() {}

    public DischargeReport(String diagnosis, String instructions, int followUpDays, LocalDate issuedDate, Admission admission) {
        this.diagnosis = diagnosis;
        this.instructions = instructions;
        this.followUpDays = followUpDays;
        this.issuedDate = issuedDate;
        this.admission = admission;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getFollowUpDays() {
        return followUpDays;
    }

    public void setFollowUpDays(int followUpDays) {
        this.followUpDays = followUpDays;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Admission getAdmission() {
        return admission;
    }

    public void setAdmission(Admission admission) {
        this.admission = admission;
    }
}
