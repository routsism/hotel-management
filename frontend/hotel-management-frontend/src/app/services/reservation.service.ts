import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface ReservationStatusReadOnlyDTO {
  id: number;
  name: string;
}

export interface ReservationReadOnlyDTO {
  id: number;
  username: string;
  roomId: number;
  roomNumber: string;
  hotelName: string;
  checkInDate: string;   
  checkOutDate: string;
  reservationStatus: ReservationStatusReadOnlyDTO;
}

export interface ReservationInsertDTO {
  userId: number;
  roomId: number;
  reservationStatusId: number;
  checkInDate: string;   
  checkOutDate: string;
}

export interface ReservationUpdateDTO {
  roomId?: number | null;
  reservationStatusId?: number | null;
  checkInDate?: string | null;
  checkOutDate?: string | null;
}

export interface UpdateReservationDatesDTO {
  newCheckIn: string;
  newCheckOut: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private baseUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient, private authService: AuthService) {}

    private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    console.log('[HTTP] Using token:', token);
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllReservations(): Observable<ReservationReadOnlyDTO[]> {
    return this.http.get<ReservationReadOnlyDTO[]>(this.baseUrl, { headers: this.getAuthHeaders() });
  }

  getReservationsByUserId(userId: number): Observable<ReservationReadOnlyDTO[]> {
    return this.http.get<ReservationReadOnlyDTO[]>(`${this.baseUrl}/user/${userId}`, { headers: this.getAuthHeaders() });
  }

  createReservation(dto: ReservationInsertDTO): Observable<ReservationReadOnlyDTO> {
    return this.http.post<ReservationReadOnlyDTO>(this.baseUrl, dto, { headers: this.getAuthHeaders() });
  }

  updateReservation(id: number, dto: ReservationUpdateDTO): Observable<ReservationReadOnlyDTO> {
  const headers = this.getAuthHeaders();
  console.log('Sending update with headers:', headers);
  return this.http.put<ReservationReadOnlyDTO>(`${this.baseUrl}/${id}`, dto, { headers });
}

  updateReservationDates(id: number, dto: UpdateReservationDatesDTO): Observable<ReservationReadOnlyDTO> {
    return this.http.put<ReservationReadOnlyDTO>(`${this.baseUrl}/${id}/dates`, dto, { headers: this.getAuthHeaders() });
  }

  cancelReservation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getAuthHeaders() });
  }
}
