import { Injectable } from '@angular/core';
import { HttpClient,  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HotelInsertDTO, HotelReadOnlyDTO } from '../models/hotel.model';
import { AuthService } from './auth.service';
import { HttpHeaders } from '@angular/common/http';

 export interface HotelResponse {
  content: HotelReadOnlyDTO[];
}



@Injectable({ providedIn: 'root' })
export class HotelService {
  private apiUrl = 'http://localhost:8080/api/hotels';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private authHeaders(): HttpHeaders {
    const token = this.authService.getToken(); 
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getAllHotels(): Observable<HotelResponse> {
  return this.http.get<HotelResponse>(this.apiUrl);
}

  getHotelById(id: number): Observable<HotelReadOnlyDTO> {
    return this.http.get<HotelReadOnlyDTO>(`${this.apiUrl}/${id}`);
  }

  createHotel(hotel: HotelInsertDTO): Observable<HotelReadOnlyDTO> {
    const headers = this.authHeaders();
    return this.http.post<HotelReadOnlyDTO>(this.apiUrl, hotel, { headers });
  }
}