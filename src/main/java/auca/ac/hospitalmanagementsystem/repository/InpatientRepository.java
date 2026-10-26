package auca.ac.hospitalmanagementsystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.ac.hospitalmanagementsystem.model.Inpatient;

@Repository
public interface InpatientRepository extends JpaRepository<Inpatient, UUID> {

    // B1: Find inpatients by lastName - DERIVED (IgnoreCase, OrderBy)
    List<Inpatient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    Optional<Inpatient> findByAdmissionNumber(String admissionNumber);

    // C4: Inpatients of a nurse - JPQL (DISTINCT, path navigation)
    @Query("SELECT DISTINCT a.inpatient FROM Admission a WHERE a.nurse.id = :nurseId")
    List<Inpatient> findInpatientsByNurseId(@Param("nurseId") UUID nurseId);
}
