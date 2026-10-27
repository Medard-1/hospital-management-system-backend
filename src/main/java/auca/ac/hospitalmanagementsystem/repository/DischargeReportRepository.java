package auca.ac.hospitalmanagementsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.ac.hospitalmanagementsystem.model.Admission;
import auca.ac.hospitalmanagementsystem.model.DischargeReport;

@Repository
public interface DischargeReportRepository extends JpaRepository<DischargeReport, UUID> {

    // E2: Discharge report history of an inpatient - JPQL (path navigation, ORDER BY)
    @Query("SELECT d FROM DischargeReport d WHERE d.admission.inpatient.admissionNumber = :admissionNumber ORDER BY d.issuedDate DESC")
    List<DischargeReport> findByInpatientAdmissionNumber(@Param("admissionNumber") String admissionNumber);

    // E3: Protect admission from deletion - DERIVED (existsByAdmission)
    boolean existsByAdmission(Admission admission);

    boolean existsByAdmissionId(UUID admissionId);
}
