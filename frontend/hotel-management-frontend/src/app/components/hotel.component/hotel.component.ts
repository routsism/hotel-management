import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelReadOnlyDTO, HotelInsertDTO } from '../../models/hotel.model';
import { HotelService, HotelResponse } from '../../services/hotel.service';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog.component/confirm-dialog.component';
import { HotelDialogComponent } from '../hotel-dialog.component/hotel-dialog.component';

@Component({
  selector: 'app-hotel',
  templateUrl: './hotel.component.html',
  styleUrls: ['./hotel.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatDividerModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
  ],
})
export class HotelComponent implements OnInit {
  hotels: HotelReadOnlyDTO[] = [];
  selectedHotel: HotelReadOnlyDTO | null = null;
  newHotel: HotelInsertDTO = {
    name: '',
    address: '',
    phone: '',
    email: '',
  };
  errorMessage = '';
  isAdmin = false;
  searchId: number | null = null;
  viewMode: 'list' | 'single' = 'list';

  constructor(
    private hotelService: HotelService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
  this.isAdmin = this.authService.isAdmin();
  console.log('Is admin:', this.isAdmin);  

  this.route.paramMap.subscribe((params) => {
    const idParam = params.get('id');
    if (idParam) {
      this.searchId = +idParam;
      this.viewMode = 'single';
      this.loadHotelById();
    } else {
      this.viewMode = 'list';
      this.loadHotels();
    }
  });
}

    

 loadHotels(): void {
  this.hotelService.getAllHotels().subscribe({
    next: (response: HotelResponse) => {
    this.hotels = response.content;
    },
    error: (err) => {
      this.errorMessage = 'Error loading hotels';
      console.error('Error loading hotels:', err);
    }
  });
}


  addHotel(): void {
  if (!this.isAdmin) {
    this.errorMessage = 'Only admin can add hotels!';
    console.log('Add hotel blocked: not admin');
    return;
  }

  if (!this.newHotel.name || !this.newHotel.address) {
    this.errorMessage = 'Name and address are required';
    console.log('Add hotel blocked: missing name or address');
    return;
  }

  console.log('Sending new hotel:', this.newHotel);

  this.hotelService.createHotel(this.newHotel).subscribe({
    next: (hotel) => {
      console.log('Hotel created:', hotel);
      this.hotels.push(hotel);
      this.newHotel = { name: '', address: '', phone: '', email: '' };
      this.errorMessage = '';

       this.snackBar.open('Hotel created successfully!', 'Close', {
        duration: 3000, 
        });
    },
    error: (err) => {
      this.errorMessage = 'Error creating hotel';
      console.error('Error creating hotel:', err);
    }
  });
}

  loadHotelById(): void {
    if (!this.searchId) {
      this.errorMessage = 'Please enter a valid hotel ID';
      this.selectedHotel = null;
      return;
    }

    this.hotelService.getHotelById(this.searchId).subscribe({
      next: (hotel: HotelReadOnlyDTO) => {
        this.selectedHotel = hotel;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = `Hotel with ID ${this.searchId} not found`;
        this.selectedHotel = null;
        console.error(err);
      },
    });
  }

  deleteHotel(hotel: HotelReadOnlyDTO): void {
  if (!this.isAdmin) {
    this.errorMessage = 'Only admin can delete hotels!';
    return;
  }

  const dialogRef = this.dialog.open(ConfirmDialogComponent, {
    width: '300px',
    data: { title: 'Confirm Deletion', message: `Are you sure you want to delete hotel "${hotel.name}"?` }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.hotelService.deleteHotel(hotel.id!).subscribe({
        next: () => {
          this.hotels = this.hotels.filter(h => h.id !== hotel.id);
          this.snackBar.open('Hotel deleted successfully!', 'Close', { duration: 3000 });
        },
        error: (err) => {
          this.errorMessage = 'Error deleting hotel';
          console.error(err);
        }
      });
    }
  });
}

editHotel(hotel: HotelReadOnlyDTO): void {
  if (!this.isAdmin) {
    this.errorMessage = 'Only admin can edit hotels!';
    return;
  }

  const dialogRef = this.dialog.open(HotelDialogComponent, {
    width: '400px',
    data: hotel,
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      const updatedHotel = { ...hotel, ...result };
      this.hotelService.updateHotel(hotel.id!, updatedHotel).subscribe({
        next: (updated) => {
          const index = this.hotels.findIndex(h => h.id === updated.id);
          if (index !== -1) this.hotels[index] = updated;
          this.snackBar.open('Hotel updated successfully!', 'Close', { duration: 3000 });
        },
        error: (err) => {
          this.errorMessage = 'Error updating hotel';
          console.error(err);
        }
      });
    }
  });
}


  backToAllHotels(): void {
    this.selectedHotel = null;
    this.loadHotels();
    this.router.navigate(['/hotels']); 
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}