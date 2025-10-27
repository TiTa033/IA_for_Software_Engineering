import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RidgeGwpService, RidgeGwpRequest, RidgeGwpResponse, RidgeGwpOptimizeResponse } from '../services/ridge-gwp.service';

@Component({
  selector: 'app-ridge-gwp-test',
  templateUrl: './ridge-gwp-test.component.html',
  styleUrls: ['./ridge-gwp-test.component.scss']
})
export class RidgeGwpTestComponent implements OnInit {
  gwpForm: FormGroup;
  predictionResult: RidgeGwpResponse | null = null;
  optimizationResult: RidgeGwpOptimizeResponse | null = null;
  loading = false;
  error: string | null = null;

  materials = [
    { value: 'cardboard', label: 'Cardboard' },
    { value: 'plastic', label: 'Plastic' },
    { value: 'wood', label: 'Wood' }
  ];

  constructor(
    private fb: FormBuilder,
    private ridgeGwpService: RidgeGwpService
  ) {
    this.gwpForm = this.fb.group({
      weight: ['', [Validators.required, Validators.min(0.01)]],
      length: ['', [Validators.required, Validators.min(0.01)]],
      width: ['', [Validators.required, Validators.min(0.01)]],
      height: ['', [Validators.required, Validators.min(0.01)]],
      distanceKm: ['', [Validators.required, Validators.min(0.01)]],
      material: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Set default values for testing
    this.gwpForm.patchValue({
      weight: 0.8,
      length: 20,
      width: 15,
      height: 10,
      distanceKm: 450,
      material: 'cardboard'
    });
  }

  onSubmit(): void {
    if (this.gwpForm.valid) {
      this.loading = true;
      this.error = null;
      this.predictionResult = null;
      this.optimizationResult = null;

      const request: RidgeGwpRequest = this.gwpForm.value;

      this.ridgeGwpService.predictGwp(request).subscribe({
        next: (response) => {
          // Handle both Spring Boot (gwp) and FastAPI (GWP) response formats
          const gwpValue = response.gwp || response.GWP || 0;
          this.predictionResult = { gwp: gwpValue };
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error predicting GWP: ' + (err.error?.message || err.message || 'Unknown error');
          this.loading = false;
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  onOptimize(): void {
    if (this.gwpForm.valid) {
      this.loading = true;
      this.error = null;
      this.optimizationResult = null;

      const request: RidgeGwpRequest = this.gwpForm.value;

      this.ridgeGwpService.optimizeGwp(request).subscribe({
        next: (response) => {
          // Handle both Spring Boot (optiWeight, optiGwp) and FastAPI (opti_weight, opti_GWP) response formats
          const optiWeightValue = response.optiWeight || response.opti_weight || 0;
          const optiGwpValue = response.optiGwp || response.opti_GWP || 0;
          this.optimizationResult = { 
            optiWeight: optiWeightValue, 
            optiGwp: optiGwpValue 
          };
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error optimizing GWP: ' + (err.error?.message || err.message || 'Unknown error');
          this.loading = false;
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.gwpForm.controls).forEach(key => {
      const control = this.gwpForm.get(key);
      control?.markAsTouched();
    });
  }

  getFieldError(fieldName: string): string {
    const field = this.gwpForm.get(fieldName);
    if (field?.errors && field.touched) {
      if (field.errors['required']) {
        return `${fieldName} is required`;
      }
      if (field.errors['min']) {
        return `${fieldName} must be greater than 0`;
      }
    }
    return '';
  }
}

