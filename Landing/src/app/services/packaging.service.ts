import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PackagingService {

  private apiUrl = 'http://localhost:8089/pfa/pfaController';  // Your backend API URL

  constructor(private http: HttpClient) { }

  // Add a new packaging
  addPackaging(packaging: { name: string, capacity: string, color: string, price: number, imageUrl?: string }, imageFile: File): Observable<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }> {
    const formData = new FormData();
    formData.append('packaging', JSON.stringify(packaging));  // Convert packaging to JSON
    formData.append('image', imageFile, imageFile.name);  // Add image file to form data

    return this.http.post<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }>(`${this.apiUrl}/add`, formData);
  }

  // Update an existing packaging
  updatePackaging(id: number, packaging: { name: string, capacity: string, color: string, price: number, imageUrl?: string }, imageFile?: File): Observable<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }> {
    const formData = new FormData();
    formData.append('packaging', JSON.stringify(packaging));
    if (imageFile) {
      formData.append('image', imageFile, imageFile.name);
    }

    return this.http.put<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }>(`${this.apiUrl}/update-package/${id}`, formData);
  }

  // Get all packaging items
  getAllPackagings(): Observable<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }[]> {
    return this.http.get<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }[]>(`${this.apiUrl}/getAll`);
  }

  // Delete a packaging by ID
  deletePackaging(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }
  retrieveById(id: number): Observable<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }> {
    return this.http.get<{ id: number, name: string, capacity: string, color: string, price: number, imageUrl?: string }>(`${this.apiUrl}/retrieve/${id}`);
  }

}
