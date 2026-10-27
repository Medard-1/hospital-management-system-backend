package auca.ac.hospitalmanagementsystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import auca.ac.hospitalmanagementsystem.model.Qualification;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, UUID> {

    Optional<Qualification> findByName(String name);

    // C3: Qualifications nobody holds - JPQL (IS EMPTY)
    @Query("SELECT q FROM Qualification q WHERE q.nurses IS EMPTY")
    List<Qualification> findUnusedQualifications();
}
