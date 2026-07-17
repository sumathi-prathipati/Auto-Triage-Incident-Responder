How to Run Your Project:
Launch the Spring Boot server (binds to Port 8080):
Open a terminal inside the 
backend folder
 and run:
bash
mvn spring-boot:run

mvn spring-boot:run
Launch the Angular frontend (serves on http://localhost:4200):
Open a terminal inside the 
frontend folder
 and run:
bash
npx ng serve --port 4200

////////////////////////
Log in with admin@responder.com / admin123.
Log in with analyst@responder.com / analyst123.
////////////////////////

Project Walkthrough: Cybersecurity Auto-Triage Incident Responder
We have built a premium, high-fidelity Full Stack Cybersecurity Operations Console. The application features a Spring Boot backend, an Angular frontend, a remote MySQL database on Hostinger, and a simulated AI/RAG Auto-Triage vulnerability analysis service.

📁 Codebase Directory Structure
The files are organized into a clean folder layout inside your workspace c:\Users\Sumathi\OneDrive\Desktop\hi\:


hi/
├── backend/                     # Spring Boot Application
│   ├── pom.xml                  # Maven Dependencies
│   └── src/main/
│       ├── java/com/cybersecurity/responder/
│       │   ├── ResponderApplication.java  # Entry point
│       │   ├── config/
│       │   │   └── DatabaseSeeder.java    # Seeds test data
│       │   ├── controller/
│       │   │   ├── AuthController.java    # Login/Signup APIs
│       │   │   ├── AdminController.java   # Alert generator/assignments
│       │   │   └── AnalystController.java # Analyst ticket queue actions
│       │   ├── dto/                       # Clean API transfers
│       │   ├── entity/                    # JPA Models (User, Alert, PredefAlert)
│       │   ├── repository/                # Data Access interfaces
│       │   ├── security/                  # Spring Security 6 & JWT 
│       │   └── service/
│       │       ├── AlertService.java      # Operations business logic
│       │       └── AutoTriageService.java # Simulated RAG CVE lookup
│       └── resources/
│           └── application.properties     # Hostinger DB & JWT configuration
│
└── frontend/                    # Angular 16 Application
    ├── src/
    │   ├── index.html             # Main index
    │   ├── styles.css             # Premium custom dark-theme console CSS
    │   └── app/
    │       ├── app.module.ts      # Registers services and components
    │       ├── app-routing.module.ts # Guards routes by role
    │       ├── models/            # Type definitions (User, Alert)
    │       ├── guards/            # AuthGuard
    │       ├── interceptors/      # JwtInterceptor (Bearer token)
    │       ├── services/          # AuthService & AlertService APIs
    │       └── components/        # Views
    │           ├── login/         # Cyber Login gateway
    │           ├── register/      # Access provisioning
    │           ├── admin-dashboard/ # Metrics, Generator, Assignments panel
    │           └── analyst-dashboard/ # Remediation playground & CVE advisor
🔑 Seeded Test Credentials
The database seeder automatically creates the following operational accounts on boot:

Role	Email	Password	Clear Level / Designation
Admin	admin@responder.com	admin123	SOC Coordinator
Analyst	analyst@responder.com	analyst123	Tier-1 Incident Responder
🚀 Running the Project Locally
Step 1: Start the Backend (Spring Boot Server)
The Spring Boot server connects to Hostinger MySQL, auto-generates schemas, seeds predefined templates, and binds to Port 8080.

Open a terminal inside c:\Users\Sumathi\OneDrive\Desktop\hi\backend
Run the command:
bash

mvn spring-boot:run
Step 2: Start the Frontend (Angular Dev Server)
The Angular application compiles with custom styling and binds to Port 4200.

Open a new terminal inside c:\Users\Sumathi\OneDrive\Desktop\hi\frontend
Run the command:
bash

npx ng serve --port 4200
Open your browser and navigate to http://localhost:4200
💻 End-to-End Operational Lifecycle Guide
To demonstrate a complete cybersecurity incident responder lifecycle in your project presentation:

Mermaid diagram
Walkthrough Steps:
Sign In as Coordinator:

Go to http://localhost:4200.
Log in with admin@responder.com / admin123.
You will see the Admin Console displaying stats: Total, Unassigned, Active, and Mitigated counts.
Trigger firewall and server alerts:

Click Generate Alert Trigger (top right).
A random threat alert will be generated and instantly triaged by the backend service. It will appear at the top of your list.
Click on the threat row. The details panel on the right will slide open, displaying the AI Auto-Triage Advisory (which performs a RAG-like lookup on a simulated CVE knowledge base, returns severity and CVE numbers, and writes an automated bash/python mitigation script).
Assign to Analyst:

Locate the generated alert. Under Assignment Action, select SOC Analyst Alpha (analyst@responder.com) from the dropdown and click Assign.
The ticket status will update to ASSIGNED in the dashboard.
Sign In as Analyst:

Log out of the admin panel.
Log in with analyst@responder.com / analyst123.
You will see the Analyst Dashboard. The ticket you assigned to this analyst will be listed in their queue.
Acknowledge and Audit:

Click on the assigned ticket.
Click Accept Incident Ticket. The ticket status will progress to ACCEPTED.
Click Initialize Active Log Audit. The status updates to INVESTIGATING.
Review the RAG advisory, copy the mitigation script, and simulated server playbooks.
Submit Remediation Report:

Scroll to the bottom of the right panel to locate Incident Remediation Documentation.
In the text area, type a summary of the actions taken (e.g. IP blocklist updated on firewalls. Loaded the generated SYN flood kernel variables to secure the web servers.).
Click Submit Mitigation Report & Close Ticket.
The ticket updates to MITIGATED and is moved to resolved status.
Verify Resolution:

Log back in as admin@responder.com.
You will observe that Mitigated Threats has increased.
Select the closed incident ticket to view the complete incident history, including the analyst's logged solution and the original AI-triaged advisory script.
