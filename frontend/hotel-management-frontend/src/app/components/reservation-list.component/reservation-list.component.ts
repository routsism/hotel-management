import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ReservationService, ReservationReadOnlyDTO } from '../../services/reservation.service';
import { AuthService } from '../../services/auth.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { ConfirmDialogComponent } from '../confirm-dialog.component/confirm-dialog.component';
import { ReservationEditDialogComponent } from '../reservation-edit-dialog.component/reservation-edit-dialog.component';
import { ReservationAddDialogComponent } from '..//reservation-add-dialog.component/reservation-add-dialog.component';


@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [CommonModule, 
    MatDialogModule, 
    MatSnackBarModule,
    MatCardModule,
    MatIconModule, 
    MatProgressSpinnerModule,
    MatDatepickerModule,       
    MatNativeDateModule, 
    MatButtonModule,
  ],
  templateUrl: './reservation-list.component.html',
  styleUrls: ['./reservation-list.component.css']
})
export class ReservationListComponent implements OnInit {

  reservations: ReservationReadOnlyDTO[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private reservationService: ReservationService,
    private authService: AuthService,
    private dialog: MatDialog,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading = true;
    this.error = null;

    if (this.authService.isAdmin() || this.authService.isEmployee()) {
      this.reservationService.getAllReservations().subscribe({
        next: data => {
          this.reservations = data;
          this.loading = false;
        },
        error: () => {
          this.error = 'Failed to load reservations';
          this.loading = false;
        }
      });
    } else if (this.authService.isGuest()) {
      const userId = this.authService.getUserId();
      if (!userId) {
        this.error = 'User ID not found';
        this.loading = false;
        return;
      }
      this.reservationService.getReservationsByUserId(userId).subscribe({
        next: data => {
          this.reservations = data;
          this.loading = false;
        },
        error: () => {
          this.error = 'Failed to load reservations';
          this.loading = false;
        }
      });
    } else {
      this.error = 'Unauthorized';
      this.loading = false;
    }
  }

  canEdit(reservation: ReservationReadOnlyDTO): boolean {
    if (this.authService.isAdmin() || this.authService.isEmployee()) {
      return true;
    }
    if (this.authService.isGuest()) {
      return reservation.username === this.authService.getUsername() && reservation.reservationStatus.name === 'PENDING';
    }
    return false;
  }

  canDelete(reservation: ReservationReadOnlyDTO): boolean {
    if (this.authService.isAdmin() || this.authService.isEmployee()) {
      return true;
    }
    if (this.authService.isGuest()) {
      return reservation.username === this.authService.getUsername();
    }
    return false;
  }

  onEdit(reservation: ReservationReadOnlyDTO): void {
    const dialogRef = this.dialog.open(ReservationEditDialogComponent, {
      width: '400px',
      data: { reservation }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'updated') {
        this.loadReservations();
        this.snackBar.open('Reservation updated successfully', 'Close', { duration: 3000 });
      }
    });
  }

  onDelete(reservation: ReservationReadOnlyDTO): void {
  const dialogRef = this.dialog.open(ConfirmDialogComponent, {
    width: '300px',
    data: {title: 'Confirm Deletion', message: `Are you sure you want to cancel reservation with id: ${reservation.id}?` }
  });

  dialogRef.afterClosed().subscribe(confirmed => {
    if (confirmed) {
      this.reservationService.cancelReservation(reservation.id).subscribe({
        next: () => {
          this.loadReservations();
          this.snackBar.open('Reservation deleted successfully', 'Close', { duration: 3000 });
        },
        error: () => {
          this.snackBar.open('Failed to cancel reservation', 'Close', { duration: 3000 });
        }
      });
    }
  });
}


  onAdd(): void {
    const userId = this.authService.getUserId(); 
    const dialogRef = this.dialog.open(ReservationAddDialogComponent, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'created') {
        this.loadReservations();
        this.snackBar.open('Reservation created successfully', 'Close', { duration: 3000 });
      }
    });
  }

  onBack(): void {
    this.router.navigate(['/dashboard']);
  }
}

