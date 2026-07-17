import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  role = 'ROLE_ANALYST'; // Default role
  loading = false;
  error = '';
  successMsg = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (!this.name || !this.email || !this.password || !this.role) {
      this.error = 'All fields are required';
      return;
    }

    if (this.password.length < 6) {
      this.error = 'Password must be at least 6 characters long';
      return;
    }

    this.loading = true;
    this.error = '';
    this.successMsg = '';

    const registerData = {
      name: this.name,
      email: this.email,
      password: this.password,
      role: this.role
    };

    this.authService.register(registerData)
      .subscribe({
        next: (response: any) => {
          this.successMsg = 'Registration successful! Redirecting to login...';
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: (err: any) => {
          this.error = err.error || 'Registration failed. Please try again.';
          this.loading = false;
        }
      });
  }
}
