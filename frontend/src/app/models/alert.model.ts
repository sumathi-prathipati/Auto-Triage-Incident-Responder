import { User } from './user.model';

export enum Severity {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export enum AlertStatus {
  UNASSIGNED = 'UNASSIGNED',
  ASSIGNED = 'ASSIGNED',
  ACCEPTED = 'ACCEPTED',
  INVESTIGATING = 'INVESTIGATING',
  MITIGATED = 'MITIGATED',
  REJECTED = 'REJECTED'
}

export interface Alert {
  id: number;
  title: string;
  description: string;
  severity: Severity;
  status: AlertStatus;
  category: string;
  timestamp: string; // ISO string from LocalDateTime
  assignedAnalyst?: User;
  aiAnalysisReport?: string;
  mitigationScript?: string;
  mitigationResult?: string;
}
