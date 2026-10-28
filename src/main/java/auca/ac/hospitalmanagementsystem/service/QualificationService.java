package auca.ac.hospitalmanagementsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.hospitalmanagementsystem.model.Qualification;
import auca.ac.hospitalmanagementsystem.repository.QualificationRepository;

@Service
public class QualificationService {

    @Autowired
    private QualificationRepository qualificationRepository;

    public String saveQualification(Qualification qualification) {
        qualificationRepository.save(qualification);
        return "Qualification saved successfully";
    }

    public List<Qualification> getAllQualifications() {
        return qualificationRepository.findAll();
    }

    public Optional<Qualification> getQualificationById(UUID id) {
        return qualificationRepository.findById(id);
    }

    public String updateQualification(UUID id, Qualification updatedQualification) {
        Optional<Qualification> existing = qualificationRepository.findById(id);
        if (existing.isEmpty()) {
            return "The qualification with that id does not exist";
        }

        Qualification qualification = existing.get();
        qualification.setName(updatedQualification.getName());
        qualificationRepository.save(qualification);
        return "Qualification updated successfully";
    }

    public String deleteQualification(UUID id) {
        if (!qualificationRepository.existsById(id)) {
            return "The qualification with that id does not exist";
        }
        qualificationRepository.deleteById(id);
        return "Qualification deleted successfully";
    }

    public List<Qualification> getUnusedQualifications() {
        return qualificationRepository.findUnusedQualifications();
    }
}
