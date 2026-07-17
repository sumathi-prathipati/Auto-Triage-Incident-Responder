export enum Role {
  ROLE_ADMIN = 'ROLE_ADMIN',
  ROLE_ANALYST = 'ROLE_ANALYST'
}

export interface User {
  id: number;
  email: string;
  name: string;
  role: Role;
  token?: string;
}
