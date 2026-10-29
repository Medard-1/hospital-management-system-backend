package auca.ac.hospitalmanagementsystem.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.hospitalmanagementsystem.model.DischargeReport;
import auca.ac.hospitalmanagementsystem.service.DischargeReportService;

@RestController
@RequestMapping(value = {"/api/discharge-reports", "/api/discharge-report"})
public class DischargeReportController {

    @Autowired
    private DischargeReportService dischargeReportService;

    // E1: Write a discharge report
    @PostMapping(value = "/save")
    public ResponseEntity<String> saveDischargeReport(@RequestParam UUID admissionId, @RequestBody DischargeReport report) {
        String message = dischargeReportService.saveDischargeReport(admissionId, report);
        if (message.equals("The admission with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("Discharge reports are only for DISCHARGED admissions")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DischargeReport>> getAllDischargeReports() {
        return new ResponseEntity<>(dischargeReportService.getAllDischargeReports(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDischargeReportById(@PathVariable UUID id) {
        Optional<DischargeReport> report = dischargeReportService.getDischargeReportById(id);
        if (report.isPresent()) {
            return new ResponseEntity<>(report.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The discharge report with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    // E2: Discharge report history of an inpatient - JPQL
    @GetMapping(value = "/inpatient/{admissionNumber}")
    public ResponseEntity<List<DischargeReport>> getDischargeReportsByInpatient(@PathVariable String admissionNumber) {
        return new ResponseEntity<>(dischargeReportService.getDischargeReportsByInpatient(admissionNumber), HttpStatus.OK);
    }

    // E3: Delete discharge report
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteDischargeReport(@PathVariable UUID id) {
        String message = dischargeReportService.deleteDischargeReport(id);
        if (message.equals("The discharge report with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
