import { Component } from '@angular/core';
import { CommonModule} from '@angular/common';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';


@Component({
  selector: 'app-dashboard',
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    RouterModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  username: string | null = '';
  role: string = '';

  constructor(private router: Router, public authService: AuthService) {}

  ngOnInit() {
   this.username = this.authService.getUsername();      
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goTo(path: string) {
    this.router.navigate(['/' + path]);
  }

  
}

