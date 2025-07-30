import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HotelReadOnlyDTO } from '../../models/hotel.model';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-hotel-dialog',
  templateUrl: './hotel-dialog.component.html',
  styleUrls: ['./hotel-dialog.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, CommonModule, ReactiveFormsModule],
})
export class HotelDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<HotelDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: HotelReadOnlyDTO
  ) {
    this.form = this.fb.group({
      name: [{ value: data.name, disabled: true }],
      address: [{ value: data.address, disabled: true }],
      phone: [data.phone || '', Validators.pattern(/^\+?\d{7,15}$/)],
      email: [data.email || '', [Validators.email]],
    });
  }

  save() {
    if (this.form.valid) {
      this.dialogRef.close({
        phone: this.form.get('phone')?.value,
        email: this.form.get('email')?.value,
      });
    }
  }

  cancel() {
    this.dialogRef.close(null);
  }
}
