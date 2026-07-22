package com.cybersecurity.responder.service;

import com.cybersecurity.responder.dto.AlertDto;
import com.cybersecurity.responder.entity.*;
import com.cybersecurity.responder.repository.AlertRepository;
import com.cybersecurity.responder.repository.PredefinedAlertRepository;
import com.cybersecurity.responder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private PredefinedAlertRepository predefinedAlertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AutoTriageService autoTriageService;

    private final Random random = new Random();
    private static final String ALERT_NOT_FOUND = "Alert not found";

    @Transactional
    public AlertDto generateRandomAlert() {
        List<PredefinedAlert> predefined = predefinedAlertRepository.findAll();
        if (predefined.isEmpty()) {
            throw new IllegalStateException("No predefined alert templates found in the database. Please seed the database first.");
        }

        // Pick random template
        PredefinedAlert template = predefined.get(random.nextInt(predefined.size()));

        // Run the simulated AI/RAG Auto-Triage process
        AutoTriageService.TriageResult triageResult = autoTriageService.triageAlert(template.getTitle(), template.getDescription());

        // Create alert entity
        Alert alert = new Alert();
        alert.setTitle(template.getTitle());
        alert.setDescription(template.getDescription());
        alert.setSeverity(triageResult.getSeverity()); // Severity assigned by triage
        alert.setCategory(template.getCategory());
        alert.setStatus(AlertStatus.UNASSIGNED);
        alert.setTimestamp(LocalDateTime.now());
        alert.setAiAnalysisReport(triageResult.getAiAnalysisReport());
        alert.setMitigationScript(triageResult.getMitigationScript());

        Alert savedAlert = alertRepository.save(alert);
        return new AlertDto(savedAlert);
    }

    @Transactional(readOnly = true)
    public List<AlertDto> getAllAlerts() {
        List<Alert> alerts = alertRepository.findAll();
        // Sort descending by timestamp
        alerts.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return alerts.stream()
                .map(AlertDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlertDto> getAlertsByAnalyst(Long analystId) {
        List<Alert> alerts = alertRepository.findByAssignedAnalystId(analystId);
        alerts.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return alerts.stream()
                .map(AlertDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlertDto> getAlertsByAnalystEmail(String email) {
        User analyst = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Analyst not found"));
        return getAlertsByAnalyst(analyst.getId());
    }

    @Transactional
    public AlertDto assignAlert(Long alertId, Long analystId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found with ID: " + alertId));
        
        User analyst = userRepository.findById(analystId)
                .orElseThrow(() -> new IllegalArgumentException("Analyst not found with ID: " + analystId));

        if (analyst.getRole() != Role.ROLE_ANALYST) {
            throw new IllegalArgumentException("User is not an Analyst. Cannot assign alerts to Admin.");
        }

        alert.setAssignedAnalyst(analyst);
        alert.setStatus(AlertStatus.ASSIGNED);
        
        Alert saved = alertRepository.save(alert);
        return new AlertDto(saved);
    }

    @Transactional
    public AlertDto acceptAlert(Long alertId, String analystEmail) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException(ALERT_NOT_FOUND));
        
        verifyAssignedAnalyst(alert, analystEmail);

        alert.setStatus(AlertStatus.ACCEPTED);
        return new AlertDto(alertRepository.save(alert));
    }

    @Transactional
    public AlertDto updateAlertStatus(Long alertId, AlertStatus status, String analystEmail) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException(ALERT_NOT_FOUND));
        
        verifyAssignedAnalyst(alert, analystEmail);

        if (status == AlertStatus.UNASSIGNED || status == AlertStatus.MITIGATED) {
            throw new IllegalArgumentException("Use correct endpoints for unassignment or resolution.");
        }

        alert.setStatus(status);
        return new AlertDto(alertRepository.save(alert));
    }

    @Transactional
    public AlertDto resolveAlert(Long alertId, String mitigationResult, String analystEmail) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException(ALERT_NOT_FOUND));
        
        verifyAssignedAnalyst(alert, analystEmail);

        if (mitigationResult == null || mitigationResult.trim().isEmpty()) {
            throw new IllegalArgumentException("Mitigation result/solution details cannot be empty.");
        }

        alert.setMitigationResult(mitigationResult);
        alert.setStatus(AlertStatus.MITIGATED);
        return new AlertDto(alertRepository.save(alert));
    }

    @Transactional
    public AlertDto rejectAlert(Long alertId, String analystEmail) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException(ALERT_NOT_FOUND));
        
        verifyAssignedAnalyst(alert, analystEmail);

        alert.setStatus(AlertStatus.REJECTED);
        alert.setAssignedAnalyst(null); // Return to pool of unassigned alerts
        alert.setStatus(AlertStatus.UNASSIGNED);
        return new AlertDto(alertRepository.save(alert));
    }

    private void verifyAssignedAnalyst(Alert alert, String email) {
        if (alert.getAssignedAnalyst() == null || !alert.getAssignedAnalyst().getEmail().equalsIgnoreCase(email)) {
            throw new IllegalStateException("You are not authorized to update this alert. It is assigned to another analyst.");
        }
    }
}
