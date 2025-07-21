import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';

interface DialogData {
  mode: 'create' | 'edit';
  user?: any;
}

@Component({
  selector: 'app-user-dialog',
   imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDialogModule
  ],
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css']
})
export class UserDialogComponent implements OnInit {
  userForm!: FormGroup;
  mode: 'create' | 'edit';
  roles = [
    { id: 1, name: 'ADMIN' },
    { id: 2, name: 'EMPLOYEE' },
    { id: 4, name: 'GUEST' }
  ];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<UserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.mode = data.mode;
  }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      username: [this.data.user?.username || '', [Validators.required, Validators.minLength(3)]],
      email: [this.data.user?.email || '', [Validators.required, Validators.email]],
      password: ['', this.mode === 'create' ? [Validators.required, Validators.minLength(6)] : []],
      roleId: [this.data.user?.role.id || '', Validators.required]
    });
  }

  onSubmit() {
    if (this.userForm.invalid) return;

    const formValue = this.userForm.value;

    // Για το update δεν στέλνουμε password αν είναι κενό
    if (this.mode === 'edit' && !formValue.password) {
      delete formValue.password;
    }

    this.dialogRef.close(formValue);
  }

  onCancel() {
    this.dialogRef.close();
  }
}

