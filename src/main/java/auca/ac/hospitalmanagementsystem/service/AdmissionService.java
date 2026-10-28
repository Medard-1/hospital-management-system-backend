package auca.ac.hospitalmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.ac.hospitalmanagementsystem.model.Admission;
import auca.ac.hospitalmanagementsystem.model.AdmissionStatus;
import auca.ac.hospitalmanagementsystem.repository.AdmissionRepository;
import auca.ac.hospitalmanagementsystem.repository.DischargeReportRepository;

@Service
public class AdmissionService {

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private DischargeReportRepository dischargeReportRepository;

    // A2 + D1: Safe saving
    public String saveAdmission(Admission admission) {
        if (admission.getAdmissionDate() != null && admission.getAdmissionDate().isBefore(LocalDate.now())) {
            return "Admission date cannot be in the past";
        }

        if (admission.getNurse() != null && admission.getNurse().getId() != null) {
            boolean alreadyBooked = admissionRepository.existsByNurseIdAndAdmissionDateAndStatusNot(
                    admission.getNurse().getId(),
                    admission.getAdmissionDate(),
                    AdmissionStatus.TRANSFERRED
            );
            if (alreadyBooked) {
                return "Nurse already receives an admission on that date";
            }
        }

        // A new admission always starts as ADMITTED, whatever status the JSON body contains
        admission.setStatus(AdmissionStatus.ADMITTED);
        admissionRepository.save(admission);
        return "Admission saved successfully";
    }

    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }

    public Optional<Admission> getAdmissionById(UUID id) {
        return admissionRepository.findById(id);
    }

    // A2 + D2: General update does not change status
    public String updateAdmission(UUID id, Admission updatedAdmission) {
        Optional<Admission> existing = admissionRepository.findById(id);
        if (existing.isEmpty()) {
            return "The admission with that id does not exist";
        }

        Admission admission = existing.get();
        admission.setAdmissionDate(updatedAdmission.getAdmissionDate());
        admission.setComplaint(updatedAdmission.getComplaint());
        if (updatedAdmission.getInpatient() != null) {
            admission.setInpatient(updatedAdmission.getInpatient());
        }
        if (updatedAdmission.getNurse() != null) {
            admission.setNurse(updatedAdmission.getNurse());
        }
        // status is not modified here
        admissionRepository.save(admission);
        return "Admission updated successfully";
    }

    // A2 + E3: Protect admission with discharge reports
    public String deleteAdmission(UUID id) {
        Optional<Admission> existing = admissionRepository.findById(id);
        if (existing.isEmpty()) {
            return "The admission with that id does not exist";
        }

        if (dischargeReportRepository.existsByAdmission(existing.get())) {
            return "That admission has discharge reports, delete them first";
        }

        admissionRepository.deleteById(id);
        return "Admission deleted successfully";
    }

    // B2: Admissions by status - DERIVED
    public List<Admission> getAdmissionsByStatus(AdmissionStatus status) {
        return admissionRepository.findByStatusOrderByAdmissionDateAsc(status);
    }

    // B3: Admissions in date range - DERIVED
    public List<Admission> getAdmissionsBetween(LocalDate start, LocalDate end) {
        return admissionRepository.findByAdmissionDateBetweenOrderByAdmissionDateAsc(start, end);
    }

    // D2: Status flow
    public String updateAdmissionStatus(UUID id, AdmissionStatus newStatus) {
        Optional<Admission> existing = admissionRepository.findById(id);
        if (existing.isEmpty()) {
            return "The admission with that id does not exist";
        }

        Admission admission = existing.get();
        AdmissionStatus current = admission.getStatus();

        boolean allowed = false;
        if (current == AdmissionStatus.ADMITTED && (newStatus == AdmissionStatus.UNDER_TREATMENT || newStatus == AdmissionStatus.TRANSFERRED)) {
            allowed = true;
        } else if (current == AdmissionStatus.UNDER_TREATMENT && (newStatus == AdmissionStatus.DISCHARGED || newStatus == AdmissionStatus.TRANSFERRED)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        admission.setStatus(newStatus);
        admissionRepository.save(admission);
        return "Admission status updated successfully";
    }

    // F1: Admissions per status - JPQL
    public List<Object[]> countAdmissionsPerStatus() {
        return admissionRepository.countAdmissionsPerStatus();
    }

    // F2: Nurse is absent - JPQL bulk update
    @Transactional
    public String cancelNurseDay(UUID nurseId, LocalDate date) {
        int count = admissionRepository.cancelNurseDay(nurseId, date);
        return count + " admissions cancelled";
    }

    // Bonus 2: Page by page
    public Page<Admission> getAdmissionsPage(Pageable pageable) {
        return admissionRepository.findAll(pageable);
    }
}
