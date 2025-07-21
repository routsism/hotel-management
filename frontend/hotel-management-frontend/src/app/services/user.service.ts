import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

  getAllUsers(): Observable<UserReadOnlyDTO[]> {
    return this.http.get<UserReadOnlyDTO[]>(this.apiUrl);
  }

  createUser(user: UserInsertDTO): Observable<UserReadOnlyDTO> {
    return this.http.post<UserReadOnlyDTO>(`${this.apiUrl}/register`, user);
  }

  updateUser(id: number, user: UserInsertDTO): Observable<UserReadOnlyDTO> {
    return this.http.put<UserReadOnlyDTO>(`${this.apiUrl}/${id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
  register(user: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }
}
