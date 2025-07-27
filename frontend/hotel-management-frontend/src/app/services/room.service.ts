import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface HotelDTO {
  id: number;
  name: string;
  address: string;
  phone?: string;
  email?: string;
}

export interface RoomTypeDTO {
  id: number;
  name: string;
}

export interface RoomReadOnlyDTO {
  id: number;
  roomNumber: string;
  hotel: HotelDTO;    
  roomType: RoomTypeDTO; 
  pricePerNight: number;
}

export interface RoomCreateUpdateDTO {
  roomNumber: string;
  hotel: HotelDTO;
  roomType: RoomTypeDTO;
  pricePerNight: number;
}




@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private baseUrl = '/api/rooms';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getAllRooms(): Observable<RoomReadOnlyDTO[]> {
  const headers = this.getAuthHeaders();
  return this.http.get<RoomReadOnlyDTO[]>(this.baseUrl, { headers });
}

  createRoom(roomData: any): Observable<RoomReadOnlyDTO> {
    const headers = this.getAuthHeaders();
    return this.http.post<RoomReadOnlyDTO>(this.baseUrl, roomData, { headers });
  }

  updateRoom(id: number, roomData: any): Observable<RoomReadOnlyDTO> {
    const headers = this.getAuthHeaders();
    return this.http.put<RoomReadOnlyDTO>(`${this.baseUrl}/${id}`, roomData, { headers });
  }

  deleteRoom(id: number): Observable<void> {
    const headers = this.getAuthHeaders();
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers });
  }
}
