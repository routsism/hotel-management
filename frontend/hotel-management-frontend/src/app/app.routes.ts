import { Routes } from '@angular/router';
import { RegisterComponent } from './components/register.component/register.component';
import { UsersComponent } from './components/users.component/users.component';
import { LoginComponent } from './components/login.component/login.component'
import { DashboardComponent } from './components/dashboard.component/dashboard.component';
import { HotelComponent } from './components/hotel.component/hotel.component';

export const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'users', component: UsersComponent },
  { path: 'hotels', component: HotelComponent },
  {
  path: 'hotels/:id',
  component: HotelComponent
},
{ path: 'users/register', component: UsersComponent },
  // { path: 'rooms', component: RoomsComponent },
  // { path: 'reservations', component: ReservationsComponent },
  { path: '', redirectTo: '/register', pathMatch: 'full' }
];