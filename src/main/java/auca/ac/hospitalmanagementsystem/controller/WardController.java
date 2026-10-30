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

import auca.ac.hospitalmanagementsystem.model.Ward;
import auca.ac.hospitalmanagementsystem.service.WardService;

@RestController
@RequestMapping(value = {"/api/wards", "/api/ward"})
public class WardController {

    @Autowired
    private WardService wardService;

    @PostMapping(value = "/save")
    public ResponseEntity<String> saveWard(@RequestBody Ward ward) {
        String message = wardService.saveWard(ward);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Ward>> getAllWards() {
        return new ResponseEntity<>(wardService.getAllWards(), HttpStatus.OK);
    }

    // C2: Free wards (JPQL)
    @GetMapping(value = "/free")
    public ResponseEntity<List<Ward>> getFreeWards() {
        return new ResponseEntity<>(wardService.getFreeWards(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getWardById(@PathVariable UUID id) {
        Optional<Ward> ward = wardService.getWardById(id);
        if (ward.isPresent()) {
            return new ResponseEntity<>(ward.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The ward with that id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> updateWard(@PathVariable UUID id, @RequestBody Ward ward) {
        String message = wardService.updateWard(id, ward);
        if (message.equals("The ward with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteWard(@PathVariable UUID id) {
        String message = wardService.deleteWard(id);
        if (message.equals("The ward with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
