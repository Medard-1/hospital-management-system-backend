package auca.ac.hospitalmanagementsystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import auca.ac.hospitalmanagementsystem.model.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, UUID> {

    // D4: Find ward by ward number - DERIVED
    Optional<Ward> findByWardNumber(int wardNumber);

    // C2: Free wards - JPQL (NOT EXISTS subquery)
    @Query("SELECT w FROM Ward w WHERE NOT EXISTS (SELECT n FROM Nurse n WHERE n.ward = w) ORDER BY w.wardNumber ASC")
    List<Ward> findFreeWards();
}
