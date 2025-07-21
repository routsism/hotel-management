import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register.component/register.component';
import { UsersComponent } from './components/users.component/users.component';
import { LoginComponent } from './components/login.component/login.component'
import { DashboardComponent } from './components/dashboard.component/dashboard.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'users', component: UsersComponent },
  { path: '', redirectTo: '/register', pathMatch: 'full' }
];