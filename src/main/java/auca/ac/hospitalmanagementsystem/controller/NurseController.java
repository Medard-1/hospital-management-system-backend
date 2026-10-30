package auca.ac.hospitalmanagementsystem.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import auca.ac.hospitalmanagementsystem.model.Nurse;
import auca.ac.hospitalmanagementsystem.service.NurseService;

@RestController
@RequestMapping(value = {"/api/nurses", "/api/nurse"})
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @PostMapping(value = "/save")
    public ResponseEntity<String> saveNurse(@RequestBody Nurse nurse) {
        String message = nurseService.saveNurse(nurse);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Nurse>> getAllNurses() {
        return new ResponseEntity<>(nurseService.getAllNurses(), HttpStatus.OK);
    }

    // C1: Nurses by qualification (JPQL)
    @GetMapping(value = "/by-qualification")
    public ResponseEntity<List<Nurse>> getNursesByQualification(@RequestParam String name) {
        return new ResponseEntity<>(nurseService.getNursesByQualification(name), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getNurseById(@PathVariable UUID id) {
        Optional<Nurse> nurse = nurseService.getNurseById(id);
        if (nurse.isPresent()) {
            return new ResponseEntity<>(nurse.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The nurse with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> updateNurse(@PathVariable UUID id, @RequestBody Nurse nurse) {
        String message = nurseService.updateNurse(id, nurse);
        if (message.equals("The nurse with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteNurse(@PathVariable UUID id) {
        String message = nurseService.deleteNurse(id);
        if (message.equals("The nurse with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D3: Add one qualification to a nurse
    @PutMapping(value = "/{nurseId}/qualifications/{qualificationId}")
    public ResponseEntity<String> addQualificationToNurse(@PathVariable UUID nurseId, @PathVariable UUID qualificationId) {
        String message = nurseService.addQualificationToNurse(nurseId, qualificationId);
        if (message.equals("The nurse with that id does not exist") || message.equals("That qualification does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("The nurse already has that qualification")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // D4: Move a nurse to another ward
    @PatchMapping(value = "/{nurseId}/ward")
    public ResponseEntity<String> changeNurseWard(@PathVariable UUID nurseId, @RequestParam int wardNumber) {
        String message = nurseService.changeNurseWard(nurseId, wardNumber);
        if (message.equals("The nurse with that id does not exist") || message.equals("The ward with that wardNumber does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("That ward is taken by another nurse")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
