package auca.ac.hospitalmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.ac.hospitalmanagementsystem.model.Admission;
import auca.ac.hospitalmanagementsystem.model.AdmissionStatus;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, UUID> {

    // B2: Admissions by status - DERIVED (enum parameter, OrderBy)
    List<Admission> findByStatusOrderByAdmissionDateAsc(AdmissionStatus status);

    // B3: Admissions in a date range - DERIVED (Between)
    List<Admission> findByAdmissionDateBetweenOrderByAdmissionDateAsc(LocalDate start, LocalDate end);

    // D1: Safe saving check - DERIVED (existsBy, And, Not)
    boolean existsByNurseIdAndAdmissionDateAndStatusNot(UUID nurseId, LocalDate admissionDate, AdmissionStatus status);

    // F1: Admissions per status - JPQL (GROUP BY, COUNT)
    @Query("SELECT a.status, COUNT(a) FROM Admission a GROUP BY a.status")
    List<Object[]> countAdmissionsPerStatus();

    // F2: Nurse is absent - JPQL (UPDATE, @Modifying)
    @Modifying
    @Query("UPDATE Admission a SET a.status = auca.ac.hospitalmanagementsystem.model.AdmissionStatus.TRANSFERRED "
         + "WHERE a.nurse.id = :nurseId "
         + "AND a.admissionDate = :date "
         + "AND a.status <> auca.ac.hospitalmanagementsystem.model.AdmissionStatus.DISCHARGED")
    int cancelNurseDay(@Param("nurseId") UUID nurseId, @Param("date") LocalDate date);

    // Bonus 1: Dashboard counts - DERIVED
    long countByAdmissionDate(LocalDate admissionDate);

    long countByStatus(AdmissionStatus status);
}
