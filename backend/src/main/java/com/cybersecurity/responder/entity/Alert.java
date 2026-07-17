package com.cybersecurity.responder.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "analyst_id")
    private User assignedAnalyst;

    @Column(columnDefinition = "TEXT")
    private String aiAnalysisReport;

    @Column(columnDefinition = "TEXT")
    private String mitigationScript;

    @Column(columnDefinition = "TEXT")
    private String mitigationResult;

    // Constructors
    public Alert() {}

    public Alert(String title, String description, Severity severity, AlertStatus status, String category, LocalDateTime timestamp) {
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.category = category;
        this.timestamp = timestamp;
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

    public User getAssignedAnalyst() {
        return assignedAnalyst;
    }

    public void setAssignedAnalyst(User assignedAnalyst) {
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
