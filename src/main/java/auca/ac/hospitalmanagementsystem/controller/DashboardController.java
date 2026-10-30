package auca.ac.hospitalmanagementsystem.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.hospitalmanagementsystem.service.DashboardService;

@RestController
@RequestMapping(value = {"/api/dashboard"})
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Bonus 1: Hospital Management System dashboard summary
    @GetMapping(value = "/summary")
    public ResponseEntity<Map<String, Long>> getSummary() {
        return new ResponseEntity<>(dashboardService.getSummary(), HttpStatus.OK);
    }
}
