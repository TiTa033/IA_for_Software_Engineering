import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class VisionService {
  constructor(private http: HttpClient) {}
  predict(file: File) {
    const form = new FormData();
    form.append('file', file);
    return this.http.post<any>('/api/vision/predict', form);
  }
}
