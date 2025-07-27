import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { UserDialogComponent } from '../user-dialog.component/user-dialog.component';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatCardModule } from '@angular/material/card';
import { UserService, UserReadOnlyDTO } from '../../services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, MatTableModule,MatCardModule, MatButtonModule, MatIconModule, MatDialogModule,MatProgressBarModule ],
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: UserReadOnlyDTO[] = [];
  loading = false;
  displayedColumns: string[] = ['username', 'email', 'role', 'actions'];

  constructor(private userService: UserService, private dialog: MatDialog, private snackBar: MatSnackBar, private router: Router) {}
 

  ngOnInit(): void {
    this.loadUsers();
  }

 loadUsers() {
  this.loading = true;
  this.userService.getAllUsers().subscribe({
    next: (data) => {
      this.users = data;
      this.loading = false;
    },
    error: (err) => {
      this.loading = false;
      console.error('Error loading users', err);
      this.snackBar.open('Error loading users', 'Close', { duration: 3000 });
    }
  });
}

  editUser(user: UserReadOnlyDTO) {
  const dialogRef = this.dialog.open(UserDialogComponent, {
    width: '400px',
    data: { mode: 'edit', user }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.userService.updateUser(user.id, result).subscribe({
        next: () => {
          this.snackBar.open('User updated successfully!', 'OK', { duration: 2000 });
          this.loadUsers();
        },
        error: () => this.snackBar.open('Failed to update user.', 'Close', { duration: 3000 })
      });
    }
  });
}

deleteUser(id: number) {
  if (confirm('Are you sure you want to delete this user?')) {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.snackBar.open('User deleted.', 'OK', { duration: 2000 });
        this.loadUsers();
      },
      error: () => this.snackBar.open('Failed to delete user.', 'Close', { duration: 3000 })
    });
  }
}

addUser() {
  // this.router.navigate(['/users/register'], { replaceUrl: false });

  const dialogRef = this.dialog.open(UserDialogComponent, {
    width: '400px',
    data: { mode: 'create' }
  });

  dialogRef.afterClosed().subscribe(result => {
    // this.router.navigate(['/users']);

    if (result) {
      this.userService.createUser(result).subscribe({
        next: () => {
          this.snackBar.open('User created successfully!', 'OK', { duration: 2000 });
          this.loadUsers();
        },
        error: () => this.snackBar.open('Failed to create user.', 'Close', { duration: 3000 })
      });
    }
  });
}

 goBack() {
    this.router.navigate(['/dashboard']);  
  }

}