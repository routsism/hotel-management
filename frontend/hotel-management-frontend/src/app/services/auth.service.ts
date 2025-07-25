import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {jwtDecode} from 'jwt-decode';

export interface AuthRequest {
  username: string;
  password: string;
}

export interface RoleDTO {
  id: number;
  name: string; // π.χ. "ADMIN", "USER"
}

export interface UserRegisterDTO {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  userId: number;
  username: string;
  email: string;
  role: RoleDTO;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly TOKEN_KEY = 'auth_token';
  private readonly USERNAME_KEY = 'auth_username';
  private readonly ROLE_KEY = 'auth_role';

  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(request: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request);
  }

  register(user: UserRegisterDTO): Observable<any> {
    return this.http.post(`${this.apiUrl.replace('/auth', '')}/users/register`, user);
  }

  saveAuthData(response: AuthResponse): void {
    localStorage.setItem(this.TOKEN_KEY, response.token);
    localStorage.setItem(this.USERNAME_KEY, response.username);
    localStorage.setItem(this.ROLE_KEY, response.role.name);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getUsername(): string | null {
    return localStorage.getItem(this.USERNAME_KEY);
  }

  getRole(): string | null {
    return localStorage.getItem(this.ROLE_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
  const token = localStorage.getItem('auth_token');
  if (!token) return false;

  const decoded: any = jwtDecode(token);
  console.log('Decoded JWT:', decoded);
  const roles: string[] = decoded.roles || decoded.authorities || [];
  console.log('Roles from token:', roles);
  return roles.includes('ADMIN') || roles.includes('ROLE_ADMIN');
}

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
    localStorage.removeItem(this.ROLE_KEY);
  }
}

