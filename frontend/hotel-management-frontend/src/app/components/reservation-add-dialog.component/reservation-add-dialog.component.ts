import { Component, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatDatepicker } from '@angular/material/datepicker';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';

import { ReservationService, ReservationInsertDTO } from '../../services/reservation.service';

@Component({
  selector: 'app-reservation-add-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
  ],
  templateUrl: './reservation-add-dialog.component.html',
  styleUrls: ['./reservation-add-dialog.component.css']
})
export class ReservationAddDialogComponent {
  @ViewChild('checkInPicker') checkInPicker!: MatDatepicker<Date>;
  @ViewChild('checkOutPicker') checkOutPicker!: MatDatepicker<Date>;

  form: FormGroup;
  loading = false;
  userId: number | null;

  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private authService: AuthService,  
    private dialogRef: MatDialogRef<ReservationAddDialogComponent>
  ) {

    this.userId = this.authService.getUserId(); 
    
    this.form = this.fb.group({
      userId: [this.userId, Validators.required],
      roomId: ['', Validators.required],
      checkInDate: ['', Validators.required],
      checkInTime: ['11:00', Validators.required],
      checkOutDate: ['',Validators.required],
      checkOutTime: ['11:00', Validators.required],
    });
  }

  openCheckInPicker() {
    this.checkInPicker.open();
  }

  openCheckOutPicker() {
    this.checkOutPicker.open();
  }

  onSubmit() {
  if (this.form.invalid) return;

  const checkInDate: Date = this.form.value.checkInDate;
  const [checkInHours, checkInMinutes] = this.form.value.checkInTime.split(':').map(Number);
  checkInDate.setHours(checkInHours, checkInMinutes, 0, 0);

  const checkOutDate: Date = this.form.value.checkOutDate;
  const [checkOutHours, checkOutMinutes] = this.form.value.checkOutTime.split(':').map(Number);
  checkOutDate.setHours(checkOutHours, checkOutMinutes, 0, 0);

  const checkInISO = checkInDate.toISOString().slice(0,19); 
  const checkOutISO = checkOutDate.toISOString().slice(0,19);

  const dto: ReservationInsertDTO = {
    userId: this.userId! ,
    roomId: this.form.value.roomId,
    reservationStatusId: 1,
    checkInDate: checkInISO,
    checkOutDate: checkOutISO
  };

  this.loading = true;
  this.reservationService.createReservation(dto).subscribe({
    next: () => {
      this.loading = false;
      this.dialogRef.close('created');
    },
    error: () => {
      this.loading = false;
      alert('Failed to create reservation');
    }
  });
}

  onCancel() {
    this.dialogRef.close();
  }
}
