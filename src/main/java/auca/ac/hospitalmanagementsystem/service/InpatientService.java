package auca.ac.hospitalmanagementsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.hospitalmanagementsystem.model.Inpatient;
import auca.ac.hospitalmanagementsystem.repository.InpatientRepository;
import auca.ac.hospitalmanagementsystem.repository.NurseRepository;

@Service
public class InpatientService {

    @Autowired
    private InpatientRepository inpatientRepository;

    @Autowired
    private NurseRepository nurseRepository;

    public String saveInpatient(Inpatient inpatient) {
        inpatientRepository.save(inpatient);
        return "Inpatient saved successfully";
    }

    public List<Inpatient> getAllInpatients() {
        return inpatientRepository.findAll();
    }

    public Optional<Inpatient> getInpatientById(UUID id) {
        return inpatientRepository.findById(id);
    }

    public String updateInpatient(UUID id, Inpatient updatedInpatient) {
        Optional<Inpatient> existing = inpatientRepository.findById(id);
        if (existing.isEmpty()) {
            return "The inpatient with that id does not exist";
        }

        Inpatient inpatient = existing.get();
        inpatient.setFirstName(updatedInpatient.getFirstName());
        inpatient.setLastName(updatedInpatient.getLastName());
        inpatient.setAdmissionNumber(updatedInpatient.getAdmissionNumber());
        inpatient.setPhone(updatedInpatient.getPhone());
        inpatient.setBloodGroup(updatedInpatient.getBloodGroup());
        inpatientRepository.save(inpatient);
        return "Inpatient updated successfully";
    }

    public String deleteInpatient(UUID id) {
        if (!inpatientRepository.existsById(id)) {
            return "The inpatient with that id does not exist";
        }
        inpatientRepository.deleteById(id);
        return "Inpatient deleted successfully";
    }

    // B1: Find inpatients by lastName - DERIVED
    public List<Inpatient> getInpatientsByLastName(String lastName) {
        return inpatientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public boolean nurseExists(UUID nurseId) {
        return nurseRepository.existsById(nurseId);
    }

    // C4: Inpatients of a nurse - JPQL
    public List<Inpatient> getInpatientsOfNurse(UUID nurseId) {
        return inpatientRepository.findInpatientsByNurseId(nurseId);
    }
}
