package com.cybersecurity.responder.dto;

import com.cybersecurity.responder.entity.Alert;
import com.cybersecurity.responder.entity.AlertStatus;
import com.cybersecurity.responder.entity.Severity;
import java.time.LocalDateTime;

public class AlertDto {
    private Long id;
    private String title;
    private String description;
    private Severity severity;
    private AlertStatus status;
    private String category;
    private LocalDateTime timestamp;
    private UserDto assignedAnalyst;
    private String aiAnalysisReport;
    private String mitigationScript;
    private String mitigationResult;

    // Constructors
    public AlertDto() {}

    public AlertDto(Alert alert) {
        if (alert != null) {
            this.id = alert.getId();
            this.title = alert.getTitle();
            this.description = alert.getDescription();
            this.severity = alert.getSeverity();
            this.status = alert.getStatus();
            this.category = alert.getCategory();
            this.timestamp = alert.getTimestamp();
            if (alert.getAssignedAnalyst() != null) {
                this.assignedAnalyst = new UserDto(alert.getAssignedAnalyst());
            }
            this.aiAnalysisReport = alert.getAiAnalysisReport();
            this.mitigationScript = alert.getMitigationScript();
            this.mitigationResult = alert.getMitigationResult();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UserDto getAssignedAnalyst() {
        return assignedAnalyst;
    }

    public void setAssignedAnalyst(UserDto assignedAnalyst) {
        this.assignedAnalyst = assignedAnalyst;
    }

    public String getAiAnalysisReport() {
        return aiAnalysisReport;
    }

    public void setAiAnalysisReport(String aiAnalysisReport) {
        this.aiAnalysisReport = aiAnalysisReport;
    }

    public String getMitigationScript() {
        return mitigationScript;
    }

    public void setMitigationScript(String mitigationScript) {
        this.mitigationScript = mitigationScript;
    }

    public String getMitigationResult() {
        return mitigationResult;
    }

    public void setMitigationResult(String mitigationResult) {
        this.mitigationResult = mitigationResult;
    }
}
