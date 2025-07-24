import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService, AuthRequest } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
   imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(loginForm: NgForm) {
    if (loginForm.invalid) {
      return; 
    }

    const req: AuthRequest = {
      username: this.username,
      password: this.password
    };

   this.authService.login(req).subscribe({
  next: (response) => {
    // this.authService.saveToken(response.token);
    // this.authService.saveUsername(response.username); 
    this.authService.saveAuthData(response);
    console.log('✅ Logged in as', response.username);
    this.router.navigate(['/dashboard']);
    },
      error: (err) => {
        if (err.status === 401) {
          this.errorMessage = 'Invalid username or password';
        } else {
          this.errorMessage = 'Server error. Please try again later.';
        }
      }
    });
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}

