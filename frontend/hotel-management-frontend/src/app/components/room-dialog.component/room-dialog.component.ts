import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { HotelService } from '../../services/hotel.service';
import { RoomService, RoomReadOnlyDTO, RoomTypeDTO, RoomCreateUpdateDTO } from '../../services/room.service';



interface HotelReadOnlyDTO {
  id: number;
  name: string;
  address: string;
  phone?: string;
  email?: string;
}



interface DialogData {
  mode: 'add' | 'edit' | 'delete';
  room?: RoomReadOnlyDTO;
}

@Component({
  selector: 'app-room-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDialogModule
  ],
  templateUrl: './room-dialog.component.html',
  styleUrls: ['./room-dialog.component.css']
})
export class RoomDialogComponent implements OnInit {
  roomForm!: FormGroup;
  mode: 'add' | 'edit' | 'delete';
  room?: RoomReadOnlyDTO;

  hotels: HotelReadOnlyDTO[] = [];
  roomTypes: RoomTypeDTO[] = [
    { id: 1, name: 'SINGLE' },
    { id: 2, name: 'DOUBLE' },
    { id: 3, name: 'SUITE' },
    { id: 4, name: 'FAMILY' },
  ];

  constructor(
    private fb: FormBuilder,
    private hotelService: HotelService,
    private dialogRef: MatDialogRef<RoomDialogComponent>,
    private roomService: RoomService,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.mode = data.mode;
    this.room = data.room;
  }

  ngOnInit(): void {
  this.loadHotels();

  if (this.mode === 'add' || this.mode === 'edit') {
    this.roomForm = this.fb.group({
      roomNumber: [this.room?.roomNumber || '', Validators.required],
      hotelId: [this.room?.hotel?.id || '', Validators.required],
      roomTypeId: [this.room?.roomType?.id || '', Validators.required],
      pricePerNight: [this.room?.pricePerNight || 0, [Validators.required, Validators.min(0.01)]],
    });

    if (this.mode === 'edit') {
      this.roomForm = this.fb.group({
      roomNumber: [this.room?.roomNumber || '', Validators.required],
      hotelId: [this.room?.hotel?.id || '', Validators.required],
      roomTypeId: [this.room?.roomType?.id || '', Validators.required],
      pricePerNight: [
        this.room?.pricePerNight || 0,
        [Validators.required, Validators.min(0.01)]
    ],
  });
  this.loadHotels();
}

  }
}

  loadHotels() {
  this.hotelService.getAllHotels().subscribe({
    next: (response) => {
      console.log('Hotels API response:', response);
      this.hotels = response.content; 
    },
    error: () => {
      this.hotels = [];
    }
  });
}

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit() {
  if (this.mode === 'delete') {
    this.dialogRef.close({ action: 'delete', id: this.room?.id });
    return;
  }

  if (this.roomForm.invalid) {
    return;
  }

  const roomData = {
    roomNumber: this.roomForm.value.roomNumber,
    hotelId: this.roomForm.value.hotelId,
    roomTypeId: this.roomForm.value.roomTypeId,
    pricePerNight: this.roomForm.value.pricePerNight
  };

  if (this.room?.id) {
    (roomData as any).id = this.room.id;
  }

  this.dialogRef.close({ action: this.mode, roomData });
}

}
