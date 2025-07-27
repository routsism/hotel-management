import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDividerModule } from '@angular/material/divider';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { RoomService, RoomReadOnlyDTO } from '../../services/room.service';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RoomDialogComponent } from '../room-dialog.component/room-dialog.component';
import { MatCardModule } from '@angular/material/card';



@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatDialogModule,
    MatSnackBarModule,
    MatDividerModule,
    MatCardModule                     
  ]
})
export class RoomComponent implements OnInit {
  rooms: RoomReadOnlyDTO[] = [];
  loading = false;
  error = '';

  constructor(
    private roomService: RoomService,
    private authService: AuthService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms() {
  this.loading = true;
  this.error = '';
  this.roomService.getAllRooms().subscribe({
    next: (rooms) => {
      console.log('Rooms loaded:', rooms);  
      this.rooms = rooms;
      this.loading = false;
    },
    error: (err) => {
      console.error('Error loading rooms:', err);
      this.error = 'Failed to load rooms.';
      this.loading = false;
    }
  });
}

  get canEditOrDelete(): boolean {
    return this.authService.isAdmin() || this.authService.isEmployee();
  }

  onAddRoom() {
  const dialogRef = this.dialog.open(RoomDialogComponent, {
    data: { mode: 'add' }
  });

  dialogRef.afterClosed().subscribe(result => {
  if (result && result.action === 'add') {
    this.roomService.createRoom(result.roomData).subscribe({
      next: createdRoom => {
        dialogRef.close();
        this.rooms = [...this.rooms, createdRoom];
        this.snackBar.open('Room successfully added!', 'Close', {
      duration: 3000,
    });
      },
      error: err => {
        console.error('Failed to create room', err);
      }
    });
  }
});
}

 onEditRoom(room: RoomReadOnlyDTO) {
  const dialogRef = this.dialog.open(RoomDialogComponent, {
    data: { mode: 'edit', room }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result && result.action === 'edit') {
      this.roomService.updateRoom(room.id, result.roomData).subscribe({
        next: updatedRoom => {
          const index = this.rooms.findIndex(r => r.id === updatedRoom.id);
          if (index !== -1) {
            this.rooms[index] = updatedRoom;
            this.rooms = [...this.rooms]; 
          }

          this.snackBar.open('Room updated successfully!', 'Close', { duration: 3000 });
        },
        error: err => {
          console.error('Failed to update room', err);
          this.snackBar.open('Failed to update room!', 'Close', { duration: 3000 });
        }
      });
    }
  });
}

  deleteRoom(room: RoomReadOnlyDTO) {
    if (!this.canEditOrDelete) {
      alert('You do not have permission to delete rooms.');
      return;
    }

    const dialogRef = this.dialog.open(RoomDialogComponent, {
      data: { mode: 'delete', room }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result?.action === 'delete') {
        this.roomService.deleteRoom(room.id).subscribe({
          next: () => {
            this.loadRooms();
            this.snackBar.open('Room successfully deleted!', 'Close', { duration: 3000 });
          },
          error: () => alert('Failed to delete room.')
        });
      }
    });
  }

  goBackToDashboard() {
    this.router.navigate(['/dashboard']);
  }
}
