import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { Alert, AlertStatus } from '../../models/alert.model';

@Component({
  selector: 'app-analyst-dashboard',
  templateUrl: './analyst-dashboard.component.html',
  styleUrls: ['./analyst-dashboard.component.css']
})
export class AnalystDashboardComponent implements OnInit {
  alerts: Alert[] = [];
  selectedAlert: Alert | null = null;
  mitigationResult = '';

  // Stats
  assignedCount = 0;
  investigatingCount = 0;
  mitigatedCount = 0;

  analystName = '';
  loading = false;
  error = '';
  success = '';

  constructor(
    private authService: AuthService,
    private alertService: AlertService,
    private router: Router
  ) {}

  ngOnInit() {
    const currentUser = this.authService.currentUserValue;
    if (currentUser) {
      this.analystName = currentUser.name;
    }
    this.loadAlerts();
  }

  loadAlerts() {
    this.loading = true;
    this.alertService.getMyAlerts().subscribe({
      next: (data: Alert[]) => {
        this.alerts = data;
        this.calculateStats();
        this.loading = false;

        // Keep current selected alert references updated
        if (this.selectedAlert) {
          const updated = this.alerts.find(a => a.id === this.selectedAlert!.id);
          this.selectedAlert = updated ? updated : null;
        }
      },
      error: (err: any) => {
        this.error = 'Failed to load assigned alerts.';
        this.loading = false;
      }
    });
  }

  calculateStats() {
    this.assignedCount = this.alerts.filter(a => a.status === AlertStatus.ASSIGNED).length;
    this.investigatingCount = this.alerts.filter(a => 
      a.status === AlertStatus.ACCEPTED || 
      a.status === AlertStatus.INVESTIGATING
    ).length;
    this.mitigatedCount = this.alerts.filter(a => a.status === AlertStatus.MITIGATED).length;
  }

  selectAlert(alert: Alert) {
    this.selectedAlert = alert;
    this.mitigationResult = alert.mitigationResult || '';
    this.error = '';
    this.success = '';
  }

  onAccept(alertId: number) {
    this.error = '';
    this.success = '';
    this.alertService.acceptAlert(alertId).subscribe({
      next: (updatedAlert: Alert) => {
        this.success = `Alert #${alertId} accepted. Ready for investigation.`;
        this.loadAlerts();
      },
      error: (err: any) => {
        this.error = 'Failed to accept alert.';
      }
    });
  }

  onReject(alertId: number) {
    if (!confirm('Are you sure you want to reject this alert? It will return to the unassigned queue.')) {
      return;
    }
    this.error = '';
    this.success = '';
    this.alertService.rejectAlert(alertId).subscribe({
      next: () => {
        this.success = `Alert #${alertId} returned to pool.`;
        this.selectedAlert = null;
        this.loadAlerts();
      },
      error: (err: any) => {
        this.error = 'Failed to reject alert.';
      }
    });
  }

  onInvestigate(alertId: number) {
    this.error = '';
    this.success = '';
    this.alertService.updateStatus(alertId, AlertStatus.INVESTIGATING).subscribe({
      next: (updated: Alert) => {
        this.success = `Incident #${alertId} status updated to INVESTIGATING.`;
        this.loadAlerts();
      },
      error: (err: any) => {
        this.error = 'Failed to update status.';
      }
    });
  }

  onResolve(alertId: number) {
    if (!this.mitigationResult.trim()) {
      alert('Please document your mitigation actions in the report text area.');
      return;
    }

    this.error = '';
    this.success = '';
    this.alertService.resolveAlert(alertId, this.mitigationResult).subscribe({
      next: (updated: Alert) => {
        this.success = `Incident #${alertId} successfully mitigated and closed.`;
        this.loadAlerts();
      },
      error: (err: any) => {
        this.error = 'Failed to submit resolution report.';
      }
    });
  }

  closeDetails() {
    this.selectedAlert = null;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
