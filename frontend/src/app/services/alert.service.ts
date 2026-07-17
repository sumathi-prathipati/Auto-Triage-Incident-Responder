import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alert, AlertStatus } from '../models/alert.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private adminUrl = 'http://localhost:8080/api/admin';
  private analystUrl = 'http://localhost:8080/api/analyst';

  constructor(private http: HttpClient) {}

  // ================= ADMIN ENDPOINTS =================

  getAllAlerts(): Observable<Alert[]> {
    return this.http.get<Alert[]>(`${this.adminUrl}/alerts`);
  }

  generateAlert(): Observable<Alert> {
    return this.http.post<Alert>(`${this.adminUrl}/alerts/generate`, {});
  }

  assignAlert(alertId: number, analystId: number): Observable<Alert> {
    return this.http.post<Alert>(`${this.adminUrl}/alerts/${alertId}/assign/${analystId}`, {});
  }

  getAnalysts(): Observable<User[]> {
    return this.http.get<User[]>(`${this.adminUrl}/analysts`);
  }

  // ================= ANALYST ENDPOINTS =================

  getMyAlerts(): Observable<Alert[]> {
    return this.http.get<Alert[]>(`${this.analystUrl}/alerts`);
  }

  acceptAlert(alertId: number): Observable<Alert> {
    return this.http.post<Alert>(`${this.analystUrl}/alerts/${alertId}/accept`, {});
  }

  rejectAlert(alertId: number): Observable<Alert> {
    return this.http.post<Alert>(`${this.analystUrl}/alerts/${alertId}/reject`, {});
  }

  updateStatus(alertId: number, status: AlertStatus): Observable<Alert> {
    // Send status as query parameter
    return this.http.post<Alert>(`${this.analystUrl}/alerts/${alertId}/status?status=${status}`, {});
  }

  resolveAlert(alertId: number, mitigationResult: string): Observable<Alert> {
    return this.http.post<Alert>(`${this.analystUrl}/alerts/${alertId}/resolve`, { mitigationResult });
  }
}
