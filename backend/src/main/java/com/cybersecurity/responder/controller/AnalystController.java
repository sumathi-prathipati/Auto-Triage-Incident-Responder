package com.cybersecurity.responder.controller;

import com.cybersecurity.responder.dto.AlertDto;
import com.cybersecurity.responder.entity.AlertStatus;
import com.cybersecurity.responder.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analyst")
public class AnalystController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/alerts")
    public ResponseEntity<List<AlertDto>> getMyAlerts(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(alertService.getAlertsByAnalystEmail(userDetails.getUsername()));
    }

    @PostMapping("/alerts/{alertId}/accept")
    public ResponseEntity<AlertDto> acceptAlert(
            @PathVariable Long alertId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            AlertDto updated = alertService.acceptAlert(alertId, userDetails.getUsername());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/alerts/{alertId}/reject")
    public ResponseEntity<AlertDto> rejectAlert(
            @PathVariable Long alertId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            AlertDto updated = alertService.rejectAlert(alertId, userDetails.getUsername());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/alerts/{alertId}/status")
    public ResponseEntity<AlertDto> updateStatus(
            @PathVariable Long alertId,
            @RequestParam AlertStatus status,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            AlertDto updated = alertService.updateAlertStatus(alertId, status, userDetails.getUsername());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/alerts/{alertId}/resolve")
    public ResponseEntity<AlertDto> resolveAlert(
            @PathVariable Long alertId,
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String mitigationResult = payload.get("mitigationResult");
            AlertDto updated = alertService.resolveAlert(alertId, mitigationResult, userDetails.getUsername());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
