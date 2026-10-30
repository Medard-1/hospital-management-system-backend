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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.hospitalmanagementsystem.model.Qualification;
import auca.ac.hospitalmanagementsystem.service.QualificationService;

@RestController
@RequestMapping(value = {"/api/qualifications", "/api/qualification"})
public class QualificationController {

    @Autowired
    private QualificationService qualificationService;

    @PostMapping(value = "/save")
    public ResponseEntity<String> saveQualification(@RequestBody Qualification qualification) {
        String message = qualificationService.saveQualification(qualification);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Qualification>> getAllQualifications() {
        return new ResponseEntity<>(qualificationService.getAllQualifications(), HttpStatus.OK);
    }

    // C3: Qualifications nobody holds (JPQL)
    @GetMapping(value = "/unused")
    public ResponseEntity<List<Qualification>> getUnusedQualifications() {
        return new ResponseEntity<>(qualificationService.getUnusedQualifications(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getQualificationById(@PathVariable UUID id) {
        Optional<Qualification> qualification = qualificationService.getQualificationById(id);
        if (qualification.isPresent()) {
            return new ResponseEntity<>(qualification.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The qualification with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> updateQualification(@PathVariable UUID id, @RequestBody Qualification qualification) {
        String message = qualificationService.updateQualification(id, qualification);
        if (message.equals("The qualification with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteQualification(@PathVariable UUID id) {
        String message = qualificationService.deleteQualification(id);
        if (message.equals("The qualification with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
