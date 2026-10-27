package auca.ac.hospitalmanagementsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.ac.hospitalmanagementsystem.model.Nurse;
import auca.ac.hospitalmanagementsystem.model.Ward;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, UUID> {

    // C1: Nurses by qualification - JPQL (JOIN on collection, LOWER)
    @Query("SELECT n FROM Nurse n JOIN n.qualifications q WHERE LOWER(q.name) = LOWER(:name)")
    List<Nurse> findNursesByQualificationName(@Param("name") String name);

    // D4: Check if ward is taken by a nurse - DERIVED (existsByWard)
    boolean existsByWard(Ward ward);
}
