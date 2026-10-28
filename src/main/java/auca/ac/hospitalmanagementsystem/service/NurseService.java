package auca.ac.hospitalmanagementsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.hospitalmanagementsystem.model.Nurse;
import auca.ac.hospitalmanagementsystem.model.Qualification;
import auca.ac.hospitalmanagementsystem.model.Ward;
import auca.ac.hospitalmanagementsystem.repository.NurseRepository;
import auca.ac.hospitalmanagementsystem.repository.QualificationRepository;
import auca.ac.hospitalmanagementsystem.repository.WardRepository;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private WardRepository wardRepository;

    public String saveNurse(Nurse nurse) {
        nurseRepository.save(nurse);
        return "Nurse saved successfully";
    }

    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    public Optional<Nurse> getNurseById(UUID id) {
        return nurseRepository.findById(id);
    }

    public String updateNurse(UUID id, Nurse updatedNurse) {
        Optional<Nurse> existing = nurseRepository.findById(id);
        if (existing.isEmpty()) {
            return "The nurse with that id does not exist";
        }

        Nurse nurse = existing.get();
        nurse.setFirstName(updatedNurse.getFirstName());
        nurse.setLastName(updatedNurse.getLastName());
        nurse.setLicenseNumber(updatedNurse.getLicenseNumber());
        if (updatedNurse.getWard() != null) {
            nurse.setWard(updatedNurse.getWard());
        }
        if (updatedNurse.getQualifications() != null) {
            nurse.setQualifications(updatedNurse.getQualifications());
        }
        nurseRepository.save(nurse);
        return "Nurse updated successfully";
    }

    public String deleteNurse(UUID id) {
        if (!nurseRepository.existsById(id)) {
            return "The nurse with that id does not exist";
        }
        nurseRepository.deleteById(id);
        return "Nurse deleted successfully";
    }

    // C1: Nurses by qualification (JPQL)
    public List<Nurse> getNursesByQualification(String name) {
        return nurseRepository.findNursesByQualificationName(name);
    }

    // D3: Add one qualification to a nurse
    public String addQualificationToNurse(UUID nurseId, UUID qualificationId) {
        Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);
        if (nurseOpt.isEmpty()) {
            return "The nurse with that id does not exist";
        }

        Optional<Qualification> qualificationOpt = qualificationRepository.findById(qualificationId);
        if (qualificationOpt.isEmpty()) {
            return "That qualification does not exist";
        }

        Nurse nurse = nurseOpt.get();
        Qualification qualification = qualificationOpt.get();

        boolean alreadyHas = nurse.getQualifications().stream()
                .anyMatch(q -> q.getId().equals(qualificationId));

        if (alreadyHas) {
            return "The nurse already has that qualification";
        }

        nurse.getQualifications().add(qualification);
        nurseRepository.save(nurse);
        return "Qualification added to the nurse";
    }

    // D4: Move a nurse to another ward
    public String changeNurseWard(UUID nurseId, int wardNumber) {
        Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);
        if (nurseOpt.isEmpty()) {
            return "The nurse with that id does not exist";
        }

        Optional<Ward> wardOpt = wardRepository.findByWardNumber(wardNumber);
        if (wardOpt.isEmpty()) {
            return "The ward with that wardNumber does not exist";
        }

        Nurse nurse = nurseOpt.get();
        Ward ward = wardOpt.get();

        boolean isTaken = nurseRepository.existsByWard(ward);
        if (isTaken) {
            return "That ward is taken by another nurse";
        }

        nurse.setWard(ward);
        nurseRepository.save(nurse);
        return "Ward changed successfully";
    }
}
