import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserReadOnlyDTO {
  id: number;
  username: string;
  email: string;
  role: {
    id: number;
    name: string;
  };
}

interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  roleId: number;
}

export interface UserInsertDTO {
  username: string;
  password: string;
  email: string;
  roleId: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllUsers(): Observable<UserReadOnlyDTO[]> {
    return this.http.get<UserReadOnlyDTO[]>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  createUser(user: UserInsertDTO): Observable<UserReadOnlyDTO> {
    return this.http.post<UserReadOnlyDTO>(
      `${this.apiUrl}/register`, 
      user, 
      { headers: this.getAuthHeaders() }
    );
  }

  updateUser(id: number, user: UserInsertDTO): Observable<UserReadOnlyDTO> {
    return this.http.put<UserReadOnlyDTO>(
      `${this.apiUrl}/${id}`, 
      user,
      { headers: this.getAuthHeaders() }
    );
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`, 
      { headers: this.getAuthHeaders() }
    );
  }
}