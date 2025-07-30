import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReservationService, ReservationUpdateDTO, ReservationReadOnlyDTO, UpdateReservationDatesDTO } from '../../services/reservation.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-reservation-edit-dialog',
  templateUrl: './reservation-edit-dialog.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
  ]
})
export class ReservationEditDialogComponent implements OnInit {
  form!: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private authService: AuthService,
    private dialogRef: MatDialogRef<ReservationEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { reservation: ReservationReadOnlyDTO }
  ) {}

  get reservation() {
    return this.data.reservation;
  }

  ngOnInit(): void {
    console.log('Is Admin:', this.authService.isAdmin());
    console.log('Can edit all fields:', this.canEditAllFields);
    console.log('Reservation data:', this.reservation);

    const checkInDate = new Date(this.reservation.checkInDate);
    const checkOutDate = new Date(this.reservation.checkOutDate);

    this.form = this.fb.group({
      roomId: [{ value: this.reservation.roomId, disabled: !(this.canEditAllFields) }, Validators.required],
      reservationStatusId: [{ value: this.reservation.reservationStatus.id, disabled: !(this.canEditAllFields) }, Validators.required],
      checkInDate: [{ value: checkInDate, disabled: !(this.canEditAllFields || this.canEditDate) }, Validators.required],
      checkInTime: [{ value: this.formatTime(checkInDate), disabled: !(this.canEditAllFields || this.canEditDate) }, Validators.required],
      checkOutDate: [{ value: checkOutDate, disabled: !(this.canEditAllFields || this.canEditDate) }, Validators.required],
      checkOutTime: [{ value: this.formatTime(checkOutDate), disabled: !(this.canEditAllFields || this.canEditDate) }, Validators.required],
    });
  }

  private formatTime(date: Date): string {
    const hours = date.getHours().toString().padStart(2,'0');
    const minutes = date.getMinutes().toString().padStart(2,'0');
    return `${hours}:${minutes}`;
  }

  public get canEditAllFields(): boolean {
    const result = this.authService.isAdmin() || this.authService.isEmployee();
    console.log('canEditAllFields called, result:', result);
    return result;
  }

  public get canEditDate(): boolean {
    const result = !this.canEditAllFields
      && this.authService.isGuest()
      && this.reservation.reservationStatus.name === 'PENDING'
      && this.reservation.username === this.authService.getUsername();
    console.log('canEditDate called, result:', result);
    return result;
  }

  private combineDateAndTime(date: Date, time: string): string {
    if (!date || !time) return '';
    const [hours, minutes] = time.split(':').map(Number);
    const combined = new Date(date);
    combined.setHours(hours, minutes, 0, 0);
    return combined.toISOString().substring(0,19);
  }

  onSave(): void {
    if (this.form.invalid) return;

    this.loading = true;

    const checkInISO = this.combineDateAndTime(this.form.get('checkInDate')!.value, this.form.get('checkInTime')!.value);
    const checkOutISO = this.combineDateAndTime(this.form.get('checkOutDate')!.value, this.form.get('checkOutTime')!.value);

    if (this.canEditAllFields) {
      const dto: ReservationUpdateDTO = {
        roomId: this.form.get('roomId')!.value,
        reservationStatusId: this.form.get('reservationStatusId')!.value,
        checkInDate: checkInISO,
        checkOutDate: checkOutISO,
      };

      this.reservationService.updateReservation(this.reservation.id, dto).subscribe({
        next: () => {
          this.loading = false;
          this.dialogRef.close('updated');
        },
        error: (err) => {
          this.loading = false;
          console.error('Update failed:', err);
          alert('Failed to update reservation');
        }
      });

    } else if (this.canEditDate) {
      const dto: UpdateReservationDatesDTO = {
        newCheckIn: checkInISO,
        newCheckOut: checkOutISO,
      };

      this.reservationService.updateReservationDates(this.reservation.id, dto).subscribe({
        next: () => {
          this.loading = false;
          this.dialogRef.close('updated');
        },
        error: (err) => {
          this.loading = false;
          console.error('Update dates failed:', err);
          alert('Failed to update reservation dates');
        }
      });

    } else {
      this.loading = false;
      alert('You do not have permission to edit this reservation');
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
