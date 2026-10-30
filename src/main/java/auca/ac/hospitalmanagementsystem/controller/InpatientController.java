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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.hospitalmanagementsystem.model.Inpatient;
import auca.ac.hospitalmanagementsystem.service.InpatientService;

@RestController
@RequestMapping(value = {"/api/inpatients", "/api/inpatient"})
public class InpatientController {

    @Autowired
    private InpatientService inpatientService;

    @PostMapping(value = "/save")
    public ResponseEntity<String> saveInpatient(@RequestBody Inpatient inpatient) {
        String message = inpatientService.saveInpatient(inpatient);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Inpatient>> getAllInpatients() {
        return new ResponseEntity<>(inpatientService.getAllInpatients(), HttpStatus.OK);
    }

    // B1: Find inpatients by lastName (DERIVED)
    @GetMapping(value = "/by-last-name")
    public ResponseEntity<List<Inpatient>> getInpatientsByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(inpatientService.getInpatientsByLastName(lastName), HttpStatus.OK);
    }

    // C4: Inpatients of a nurse (JPQL)
    @GetMapping(value = "/of-nurse/{nurseId}")
    public ResponseEntity<?> getInpatientsOfNurse(@PathVariable UUID nurseId) {
        if (!inpatientService.nurseExists(nurseId)) {
            return new ResponseEntity<>("The nurse with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(inpatientService.getInpatientsOfNurse(nurseId), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getInpatientById(@PathVariable UUID id) {
        Optional<Inpatient> inpatient = inpatientService.getInpatientById(id);
        if (inpatient.isPresent()) {
            return new ResponseEntity<>(inpatient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The inpatient with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> updateInpatient(@PathVariable UUID id, @RequestBody Inpatient inpatient) {
        String message = inpatientService.updateInpatient(id, inpatient);
        if (message.equals("The inpatient with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteInpatient(@PathVariable UUID id) {
        String message = inpatientService.deleteInpatient(id);
        if (message.equals("The inpatient with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
