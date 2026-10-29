package auca.ac.hospitalmanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.hospitalmanagementsystem.model.Admission;
import auca.ac.hospitalmanagementsystem.model.AdmissionStatus;
import auca.ac.hospitalmanagementsystem.service.AdmissionService;

@RestController
@RequestMapping(value = {"/api/admissions", "/api/admission"})
public class AdmissionController {

    @Autowired
    private AdmissionService admissionService;

    // A2 + D1: Safe saving
    @PostMapping(value = "/save")
    public ResponseEntity<String> saveAdmission(@RequestBody Admission admission) {
        String message = admissionService.saveAdmission(admission);
        if (message.equals("Admission date cannot be in the past")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if (message.equals("Nurse already receives an admission on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Admission>> getAllAdmissions() {
        return new ResponseEntity<>(admissionService.getAllAdmissions(), HttpStatus.OK);
    }

    // B2: Admissions by status - DERIVED
    @GetMapping(value = "/by-status")
    public ResponseEntity<List<Admission>> getAdmissionsByStatus(@RequestParam AdmissionStatus status) {
        return new ResponseEntity<>(admissionService.getAdmissionsByStatus(status), HttpStatus.OK);
    }

    // B3: Admissions in a date range - DERIVED
    @GetMapping(value = "/between")
    public ResponseEntity<List<Admission>> getAdmissionsBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return new ResponseEntity<>(admissionService.getAdmissionsBetween(startDate, endDate), HttpStatus.OK);
    }

    // F1: Admissions per status - JPQL
    @GetMapping(value = "/stats/by-status")
    public ResponseEntity<List<Object[]>> countAdmissionsPerStatus() {
        return new ResponseEntity<>(admissionService.countAdmissionsPerStatus(), HttpStatus.OK);
    }

    // F2: Nurse is absent - JPQL bulk update
    @PatchMapping(value = "/cancel-day")
    public ResponseEntity<String> cancelNurseDay(@RequestParam UUID nurseId, @RequestParam String date) {
        LocalDate day = LocalDate.parse(date);
        String message = admissionService.cancelNurseDay(nurseId, day);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Bonus 2: Admissions page by page
    @GetMapping(value = "/page")
    public ResponseEntity<Page<Admission>> getAdmissionsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "admissionDate,desc") String sort) {
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParts.length > 1) {
            direction = Sort.Direction.fromString(sortParts[1]);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, field));
        return new ResponseEntity<>(admissionService.getAdmissionsPage(pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAdmissionById(@PathVariable UUID id) {
        Optional<Admission> admission = admissionService.getAdmissionById(id);
        if (admission.isPresent()) {
            return new ResponseEntity<>(admission.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The admission with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    // A2 + D2: General update does not change status
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> updateAdmission(@PathVariable UUID id, @RequestBody Admission admission) {
        String message = admissionService.updateAdmission(id, admission);
        if (message.equals("The admission with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D2: Status flow
    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<String> updateAdmissionStatus(@PathVariable UUID id, @RequestParam AdmissionStatus status) {
        String message = admissionService.updateAdmissionStatus(id, status);
        if (message.equals("The admission with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.startsWith("Cannot change status from ")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // A2 + E3: Protect admission from deletion if it has discharge reports
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteAdmission(@PathVariable UUID id) {
        String message = admissionService.deleteAdmission(id);
        if (message.equals("The admission with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That admission has discharge reports, delete them first")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
