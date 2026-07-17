import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { Alert, AlertStatus } from '../../models/alert.model';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  alerts: Alert[] = [];
  analysts: User[] = [];
  selectedAnalystForAlert: { [alertId: number]: number } = {};
  
  // Stats
  totalCount = 0;
  unassignedCount = 0;
  assignedCount = 0;
  mitigatedCount = 0;

  // Selected Alert for Details Modal/Drawer
  selectedAlert: Alert | null = null;

  operatorName = '';
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
      this.operatorName = currentUser.name;
    }
    this.loadData();
  }

  loadData() {
    this.loading = true;
    this.alertService.getAllAlerts().subscribe({
      next: (data: Alert[]) => {
        this.alerts = data;
        this.calculateStats();
        this.loading = false;
      },
      error: (err: any) => {
        this.error = 'Failed to load security alerts.';
        this.loading = false;
      }
    });

    this.alertService.getAnalysts().subscribe({
      next: (data: User[]) => {
        this.analysts = data;
      },
      error: (err: any) => {
        this.error = 'Failed to load list of analysts.';
      }
    });
  }

  calculateStats() {
    this.totalCount = this.alerts.length;
    this.unassignedCount = this.alerts.filter(a => a.status === AlertStatus.UNASSIGNED).length;
    this.assignedCount = this.alerts.filter(a => 
      a.status === AlertStatus.ASSIGNED || 
      a.status === AlertStatus.ACCEPTED || 
      a.status === AlertStatus.INVESTIGATING
    ).length;
    this.mitigatedCount = this.alerts.filter(a => a.status === AlertStatus.MITIGATED).length;
  }

  onGenerateAlert() {
    this.loading = true;
    this.error = '';
    this.success = '';
    this.alertService.generateAlert().subscribe({
      next: (newAlert: Alert) => {
        this.success = `System Alert [${newAlert.id}] generated and triaged.`;
        this.loadData();
      },
      error: (err: any) => {
        this.error = err.error || 'Failed to generate random system alert.';
        this.loading = false;
      }
    });
  }

  onAssign(alertId: number) {
    const analystId = this.selectedAnalystForAlert[alertId];
    if (!analystId) {
      alert('Please select an analyst first.');
      return;
    }

    this.error = '';
    this.success = '';
    this.alertService.assignAlert(alertId, analystId).subscribe({
      next: (updatedAlert: Alert) => {
        this.success = `Alert [${alertId}] successfully assigned to analyst.`;
        this.loadData();
      },
      error: (err: any) => {
        this.error = 'Failed to assign alert.';
      }
    });
  }

  selectAlert(alert: Alert) {
    this.selectedAlert = alert;
  }

  closeDetailPanel() {
    this.selectedAlert = null;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
