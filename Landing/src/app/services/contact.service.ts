import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Contact {
  id?: number;
  name: string;
  email: string;
  subject: string;
  comments: string;
  createdAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  private apiUrl = 'http://localhost:8089/pfa/contact';  // Backend contact API URL

  constructor(private http: HttpClient) { }

  // Submit contact form
  submitContact(contact: Contact): Observable<Contact> {
    return this.http.post<Contact>(`${this.apiUrl}/submit`, contact);
  }

  // Get all contacts (for admin purposes)
  getAllContacts(): Observable<Contact[]> {
    return this.http.get<Contact[]>(`${this.apiUrl}/all`);
  }

  // Get contact by ID
  getContactById(id: number): Observable<Contact> {
    return this.http.get<Contact>(`${this.apiUrl}/${id}`);
  }

  // Delete contact by ID
  deleteContact(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
