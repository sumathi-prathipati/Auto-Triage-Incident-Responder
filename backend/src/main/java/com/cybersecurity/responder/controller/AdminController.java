package com.cybersecurity.responder.controller;

import com.cybersecurity.responder.dto.AlertDto;
import com.cybersecurity.responder.dto.UserDto;
import com.cybersecurity.responder.entity.Role;
import com.cybersecurity.responder.entity.User;
import com.cybersecurity.responder.repository.UserRepository;
import com.cybersecurity.responder.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/alerts")
    public ResponseEntity<List<AlertDto>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @PostMapping("/alerts/generate")
    public ResponseEntity<AlertDto> generateAlert() {
        return ResponseEntity.ok(alertService.generateRandomAlert());
    }

    @PostMapping("/alerts/{alertId}/assign/{analystId}")
    public ResponseEntity<AlertDto> assignAlert(@PathVariable Long alertId, @PathVariable Long analystId) {
        try {
            AlertDto updated = alertService.assignAlert(alertId, analystId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/analysts")
    public ResponseEntity<List<UserDto>> getAnalysts() {
        List<User> analysts = userRepository.findByRole(Role.ROLE_ANALYST);
        List<UserDto> dtos = analysts.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
