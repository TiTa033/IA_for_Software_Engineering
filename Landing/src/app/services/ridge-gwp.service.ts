import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RidgeGwpRequest {
  weight: number;
  length: number;
  width: number;
  height: number;
  distanceKm: number;
  material: string;
}

export interface RidgeGwpResponse {
  gwp: number;
  GWP?: number;  // Fallback for FastAPI response
}

export interface RidgeGwpOptimizeResponse {
  optiWeight: number;
  optiGwp: number;
  opti_weight?: number;  // Fallback for FastAPI response
  opti_GWP?: number;    // Fallback for FastAPI response
}

@Injectable({
  providedIn: 'root'
})
export class RidgeGwpService {
  private baseUrl = 'http://localhost:8089/pfa/api/ridge-gwp';

  constructor(private http: HttpClient) { }

  predictGwp(request: RidgeGwpRequest): Observable<RidgeGwpResponse> {
    return this.http.post<RidgeGwpResponse>(`${this.baseUrl}/predict`, request);
  }

  optimizeGwp(request: RidgeGwpRequest): Observable<RidgeGwpOptimizeResponse> {
    return this.http.post<RidgeGwpOptimizeResponse>(`${this.baseUrl}/optimize`, request);
  }

  healthCheck(): Observable<string> {
    return this.http.get(`${this.baseUrl}/health`, { responseType: 'text' });
  }
}

