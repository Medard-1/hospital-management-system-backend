package auca.ac.hospitalmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.hospitalmanagementsystem.model.Admission;
import auca.ac.hospitalmanagementsystem.model.AdmissionStatus;
import auca.ac.hospitalmanagementsystem.model.DischargeReport;
import auca.ac.hospitalmanagementsystem.repository.AdmissionRepository;
import auca.ac.hospitalmanagementsystem.repository.DischargeReportRepository;

@Service
public class DischargeReportService {

    @Autowired
    private DischargeReportRepository dischargeReportRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    // E1: Write a discharge report
    public String saveDischargeReport(UUID admissionId, DischargeReport report) {
        Optional<Admission> admissionOpt = admissionRepository.findById(admissionId);
        if (admissionOpt.isEmpty()) {
            return "The admission with that id does not exist";
        }

        Admission admission = admissionOpt.get();
        if (admission.getStatus() != AdmissionStatus.DISCHARGED) {
            return "Discharge reports are only for DISCHARGED admissions";
        }

        report.setIssuedDate(LocalDate.now());
        report.setAdmission(admission);
        dischargeReportRepository.save(report);
        return "Discharge report saved successfully";
    }

    public List<DischargeReport> getAllDischargeReports() {
        return dischargeReportRepository.findAll();
    }

    public Optional<DischargeReport> getDischargeReportById(UUID id) {
        return dischargeReportRepository.findById(id);
    }

    // E2: Discharge report history of an inpatient - JPQL
    public List<DischargeReport> getDischargeReportsByInpatient(String admissionNumber) {
        return dischargeReportRepository.findByInpatientAdmissionNumber(admissionNumber);
    }

    // E3: Delete discharge report
    public String deleteDischargeReport(UUID id) {
        if (!dischargeReportRepository.existsById(id)) {
            return "The discharge report with that id does not exist";
        }
        dischargeReportRepository.deleteById(id);
        return "Discharge report deleted successfully";
    }
}
