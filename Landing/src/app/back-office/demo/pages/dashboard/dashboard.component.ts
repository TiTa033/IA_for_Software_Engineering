// angular import
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

// project import
import { SharedModule } from 'src/app/back-office/demo/shared/shared.module';
import { NgApexchartsModule } from 'ng-apexcharts';
import { PackagingService } from '../../../../services/packaging.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, SharedModule, NgApexchartsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export default class DashboardComponent implements OnInit {

  packaging = { name: '', capacity: '', color: '', price: 0, imageUrl: '' };
  imageFile: File | null = null;
  allPackagings: any[] = [];
  filteredPackagings: any[] = [];
  maxPrice: number = 100;
  searchTerm: string = '';

  isEditMode: boolean = false;
  editPackagingId: number | null = null;

  constructor(private packagingService: PackagingService) { }

  ngOnInit(): void {
    this.loadAllPackagings();
  }

  filterByPrice() {
    const lowerSearch = this.searchTerm.toLowerCase();
    this.filteredPackagings = this.allPackagings.filter(p =>
      p.price <= this.maxPrice &&
      p.name.toLowerCase().includes(lowerSearch)
    );
  }

  onFileSelected(event: any) {
    this.imageFile = event.target.files[0];
  }

  addOrUpdatePackaging() {
    if (this.isEditMode && this.editPackagingId !== null) {
      this.updatePackaging(this.editPackagingId);
    } else {
      this.addPackaging();
    }
  }

  addPackaging() {
    if (this.imageFile) {
      this.packagingService.addPackaging(this.packaging, this.imageFile).subscribe(
        (response) => {
          this.resetForm();
          this.loadAllPackagings();
        },
        (error) => {
          console.error('Error adding packaging:', error);
        }
      );
    }
  }

  updatePackaging(id: number) {
    if (this.imageFile || this.packaging.imageUrl) {
      this.packagingService.updatePackaging(id, this.packaging, this.imageFile ?? undefined).subscribe(
        (response) => {
          this.resetForm();
          this.loadAllPackagings();
        },
        (error) => {
          console.error('Error updating packaging:', error);
        }
      );
    } else {
      alert('Please select an image or keep the existing one.');
    }
  }

  deletePackaging(id: number) {
    this.packagingService.deletePackaging(id).subscribe(
      () => this.loadAllPackagings(),
      (error) => console.error('Error deleting packaging:', error)
    );
  }

  editPackaging(packaging: any) {
    this.packaging = { ...packaging }; // clone to avoid two-way binding issues
    this.isEditMode = true;
    this.editPackagingId = packaging.id;
    this.imageFile = null;
  }

  resetForm() {
    this.packaging = { name: '', capacity: '', color: '', price: 0, imageUrl: '' };
    this.imageFile = null;
    this.isEditMode = false;
    this.editPackagingId = null;
  }

  loadAllPackagings() {
    this.packagingService.getAllPackagings().subscribe((data: any[]) => {
      this.allPackagings = data;
      this.filterByPrice();
    });
  }

  getFormattedCapacity(capacity: string): string {
    switch (capacity) {
      case 'I_250_ML': return '250mL';
      case 'II_500_ML': return '500mL';
      case 'III_750_ML': return '750mL';
      case 'IV_1_L': return '1L';
      default: return capacity;
    }
  }

  selectedPackaging: any = null;
  showDetails(id: number) {
    this.packagingService.retrieveById(id).subscribe(
      data => this.selectedPackaging = data,
      error => console.error('Error retrieving packaging:', error)
    );
  }

  closeModal() {
    this.selectedPackaging = null;
  }
}
