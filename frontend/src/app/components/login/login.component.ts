import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  email = '';
  password = '';
  loading = false;
  error = '';
  returnUrl = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // redirect to home if already logged in
    const currentUser = this.authService.currentUserValue;
    if (currentUser) {
      if (currentUser.role === 'ROLE_ADMIN') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/analyst']);
      }
    }

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  onSubmit() {
    if (!this.email || !this.password) {
      this.error = 'Please fill in all fields';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.login(this.email, this.password)
      .subscribe({
        next: (user: any) => {
          if (user.role === 'ROLE_ADMIN') {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/analyst']);
          }
        },
        error: (err: any) => {
          this.error = err.error || 'Authentication failed. Please check your credentials.';
          this.loading = false;
        }
      });
  }
}
