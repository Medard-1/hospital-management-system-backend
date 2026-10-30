package auca.ac.hospitalmanagementsystem.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.hospitalmanagementsystem.model.AdmissionStatus;
import auca.ac.hospitalmanagementsystem.repository.AdmissionRepository;
import auca.ac.hospitalmanagementsystem.repository.DischargeReportRepository;
import auca.ac.hospitalmanagementsystem.repository.InpatientRepository;
import auca.ac.hospitalmanagementsystem.repository.NurseRepository;
import auca.ac.hospitalmanagementsystem.repository.WardRepository;

@Service
public class DashboardService {

    @Autowired
    private InpatientRepository inpatientRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private WardRepository wardRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private DischargeReportRepository dischargeReportRepository;

    // Bonus 1: Hospital Management System dashboard summary
    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("inpatients", inpatientRepository.count());
        summary.put("nurses", nurseRepository.count());
        summary.put("freeWards", (long) wardRepository.findFreeWards().size());
        summary.put("admissionsToday", admissionRepository.countByAdmissionDate(LocalDate.now()));
        summary.put("admitted", admissionRepository.countByStatus(AdmissionStatus.ADMITTED));
        summary.put("dischargeReports", dischargeReportRepository.count());
        return summary;
    }
}
