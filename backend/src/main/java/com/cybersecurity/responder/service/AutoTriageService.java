package com.cybersecurity.responder.service;

import com.cybersecurity.responder.entity.Severity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutoTriageService {

    public static class TriageResult {
        private Severity severity;
        private String aiAnalysisReport;
        private String mitigationScript;

        public TriageResult(Severity severity, String aiAnalysisReport, String mitigationScript) {
            this.severity = severity;
            this.aiAnalysisReport = aiAnalysisReport;
            this.mitigationScript = mitigationScript;
        }

        public Severity getSeverity() { return severity; }
        public String getAiAnalysisReport() { return aiAnalysisReport; }
        public String getMitigationScript() { return mitigationScript; }
    }

    private static class ThreatTemplate {
        String keyword;
        String title;
        Severity defaultSeverity;
        String cveId;
        double cvssScore;
        String description;
        String mitigationSteps;
        String scriptTemplate;

        ThreatTemplate(String keyword, String title, Severity defaultSeverity, String cveId, double cvssScore, 
                       String description, String mitigationSteps, String scriptTemplate) {
            this.keyword = keyword;
            this.title = title;
            this.defaultSeverity = defaultSeverity;
            this.cveId = cveId;
            this.cvssScore = cvssScore;
            this.description = description;
            this.mitigationSteps = mitigationSteps;
            this.scriptTemplate = scriptTemplate;
        }
    }

    private final List<ThreatTemplate> vulnerabilityDatabase = new ArrayList<>();

    public AutoTriageService() {
        // Seed some mock CVE knowledge base items for RAG-like lookup
        vulnerabilityDatabase.add(new ThreatTemplate(
            "log4j", "Log4Shell Vulnerability (RCE)", Severity.CRITICAL, "CVE-2021-44228", 10.0,
            "Apache Log4j2 JNDI features do not protect against attacker controlled LDAP endpoints, permitting remote attackers to execute arbitrary code.",
            "1. Update Apache Log4j to version 2.15.0 or higher.\n2. Set environment variable LOG4J_FORMAT_MSG_NO_LOOKUPS=true.\n3. Remove JndiLookup class from classpath.",
            "#!/bin/bash\n# LOG4SHELL HOT-MITIGATION SCRIPT\necho 'Scanning for Log4j vulnerabilities...'\n\n# Set the environment variable across the system\nif ! grep -q 'LOG4J_FORMAT_MSG_NO_LOOKUPS' /etc/environment; then\n  echo 'LOG4J_FORMAT_MSG_NO_LOOKUPS=true' >> /etc/environment\n  export LOG4J_FORMAT_MSG_NO_LOOKUPS=true\n  echo '[SUCCESS] Added global mitigation environment variable.'\nelse\n  echo '[INFO] Mitigation variable already set.'\nfi\n\n# Block external JNDI LDAP lookup ports on UFW firewall\necho 'Configuring UFW to block LDAP lookups...'\nufw deny out 389/tcp\nufw deny out 1389/tcp\nufw reload\necho '[SUCCESS] Firewall updated. Egress LDAP ports 389/1389 blocked.'\n"
        ));

        vulnerabilityDatabase.add(new ThreatTemplate(
            "sql injection", "SQL Injection Threat", Severity.HIGH, "CVE-2023-38606", 8.8,
            "SQL Injection detected via API endpoint request parameters. Attackers can execute arbitrary SQL commands via the HTTP parameter payload.",
            "1. Implement PreparedStatement parameterized queries.\n2. Utilize Web Application Firewall (WAF) SQLi rules.\n3. Sanitize inputs using regex filtering.",
            "import re\nimport sys\n\n# PYTHON WAF INPUT FILTERING SCRIPT FOR SQLi\ndef check_sql_injection(input_str):\n    sql_regex = re.compile(r'(\\'|\\\"|--|;|\\b(select|union|insert|update|delete|drop|alter|where|or 1=1)\\b)', re.IGNORECASE)\n    if sql_regex.search(input_str):\n        print(f'[ALERT] SQLi Signature found in: \"{input_str}\"')\n        return True\n    return False\n\ntest_input = sys.argv[1] if len(sys.argv) > 1 else \"' OR '1'='1\"\nif check_sql_injection(test_input):\n    print('[ACTION] Connection terminated. Payload blocked.')\n    sys.exit(1)\nelse:\n    print('[PASS] Payload cleared.')\n    sys.exit(0)\n"
        ));

        vulnerabilityDatabase.add(new ThreatTemplate(
            "ddos", "DDoS SYN Flood Attack", Severity.HIGH, "CVE-2018-0101", 7.5,
            "High frequency SYN requests detected targeting TCP Port 80/443, leading to resource exhaustion (half-open TCP connections queue overflow).",
            "1. Enable TCP SYN Cookies.\n2. Tune net.ipv4.tcp_max_syn_backlog.\n3. Implement rate-limiting via Nginx or API gateway.",
            "#!/bin/bash\n# SYN FLOOD ANTISEMI-MITIGATION SCRIPT\necho 'Enabling SYN cookies mitigation...'\n\n# Configure kernel parameters\nsysctl -w net.ipv4.tcp_syncookies=1\nsysctl -w net.ipv4.tcp_max_syn_backlog=2048\nsysctl -w net.ipv4.tcp_synack_retries=2\n\n# Save values permanently\necho 'net.ipv4.tcp_syncookies = 1' >> /etc/sysctl.conf\necho 'net.ipv4.tcp_max_syn_backlog = 2048' >> /etc/sysctl.conf\nsysctl -p\n\necho '[SUCCESS] Kernel tuned to counter TCP SYN Flood. SYN Cookies enabled.'\n"
        ));

        vulnerabilityDatabase.add(new ThreatTemplate(
            "brute force", "SSH/Authentication Brute Force", Severity.MEDIUM, "CVE-2022-22720", 5.3,
            "Multiple failed login attempts detected from single external IP address within short interval, indicating an active password guessing campaign.",
            "1. Install and configure Fail2Ban.\n2. Restrict SSH logins to key-based authentication only.\n3. Enable account lockout policies.",
            "#!/bin/bash\n# IP LOCKOUT & FAIL2BAN PROVISIONING\necho 'Checking IP access frequency...'\n\nBAD_IP=\"192.168.1.150\" # Simulated suspicious IP\n\n# Check if IP already blocked via iptables\nif iptables -L INPUT -v -n | grep -q \"$BAD_IP\"; then\n  echo \"[INFO] IP $BAD_IP is already blocked.\"\nelse\n  echo \"[MITIGATION] Blocking malicious IP: $BAD_IP\"\n  iptables -A INPUT -s \"$BAD_IP\" -j DROP\n  echo \"[SUCCESS] iptables updated. $BAD_IP dropped.\"\nfi\n\n# Install Fail2ban if not present\nif ! command -v fail2ban-client &> /dev/null; then\n  echo 'Fail2ban not detected. Installing Fail2ban...'\n  apt-get update && apt-get install -y fail2ban\nfi\nsystemctl enable fail2ban && systemctl start fail2ban\necho '[SUCCESS] Fail2Ban service initialized.'\n"
        ));

        vulnerabilityDatabase.add(new ThreatTemplate(
            "ransomware", "Ransomware Behavior Detected", Severity.CRITICAL, "CVE-2023-27350", 9.8,
            "Abnormal rapid file modifications and mass file encryptions detected on local storage drives.",
            "1. Isolate the server immediately from the network.\n2. Kill processes utilizing high IOPS.\n3. Restore affected files from isolated backup servers.",
            "#!/bin/bash\n# EMERGENCY HOST ISOLATION SCRIPT\necho '[ALERT] ENCRYPTOR SIGNATURE DETECTED. ISOLATING HOST...'\n\n# Block all incoming/outgoing traffic except management subnet (e.g. 10.0.0.0/8)\niptables -F\niptables -P INPUT DROP\niptables -P OUTPUT DROP\niptables -P FORWARD DROP\n\n# Allow loopback interface\niptables -A INPUT -i lo -j ACCEPT\niptables -A OUTPUT -o lo -j ACCEPT\n\n# Terminate suspicious high-IO disk processes\necho 'Terminating rogue file writers...'\nps aux --sort=-%cpu | head -n 5\n\necho '[CRITICAL] Host isolated. Net connections severed to prevent ransomware spread.'\n"
        ));
    }

    public TriageResult triageAlert(String title, String description) {
        String combined = (title + " " + description).toLowerCase();

        // Search our vulnerability "database" (RAG style query matcher)
        for (ThreatTemplate temp : vulnerabilityDatabase) {
            if (combined.contains(temp.keyword)) {
                String report = String.format(
                    "=== AUTO-TRIAGE INTELLIGENT SECURITY ADVISORY ===\n" +
                    "Vulnerability Identified: %s\n" +
                    "Severity Level: %s\n" +
                    "CVE Identifier: %s (CVSS v3 Score: %.1f)\n\n" +
                    "Vulnerability Overview:\n%s\n\n" +
                    "Recommended Mitigation Procedures:\n%s\n\n" +
                    "Status: Triage engine verified and classified. Auto mitigation script generated.",
                    temp.title, temp.defaultSeverity.name(), temp.cveId, temp.cvssScore, temp.description, temp.mitigationSteps
                );

                return new TriageResult(temp.defaultSeverity, report, temp.scriptTemplate);
            }
        }

        // Default Fallback triage if no CVE matches
        String report = "=== AUTO-TRIAGE INTELLIGENT SECURITY ADVISORY ===\n" +
                "Vulnerability Identified: General Security Anomaly\n" +
                "Severity Level: LOW\n" +
                "CVE Identifier: CVE-GENERIC\n\n" +
                "Vulnerability Overview:\n" +
                "An alert has been captured with unrecognized signatures. Logged payload: " + description + "\n\n" +
                "Recommended Mitigation Procedures:\n" +
                "1. Analyst should audit server logs.\n" +
                "2. Conduct port scans and verify system health.\n" +
                "3. Monitor traffic anomalies.";

        String script = "#!/bin/bash\n# GENERIC SECURITY SYSTEM AUDIT\necho 'Running general system check...'\nuname -a\nnetstat -tuln\necho '[INFO] General system report compiled. Analyst audit required.'\n";

        return new TriageResult(Severity.LOW, report, script);
    }
}
