import { Component, OnInit } from '@angular/core';
import { PackagingService } from 'src/app/services/packaging.service';  // Adjust path as needed
import { Observable, of } from 'rxjs';  // Import 'of' from RxJS
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-features',
  templateUrl: './features.component.html',
  styleUrls: ['./features.component.scss']
})
export class FeaturesComponent implements OnInit {

  // Variables for packaging form
  packaging = { name: '', capacity: '', color: '', price: 0 };  // Initial empty packaging object
  imageFile: File | null = null;  // File input for image
  allPackagings: any[] = []; // Full list (non-observable now)
  filteredPackagings: any[] = []; // Filtered list for display
  maxPrice: number = 100; // Initial slider value
  searchTerm: string = ''; // New variable for search input

  constructor(private packagingService: PackagingService) { }

  ngOnInit(): void {
    this.loadAllPackagings();  // Load all packagings when the component initializes
  }
  filterByPrice() {
    const lowerSearch = this.searchTerm.toLowerCase();
    this.filteredPackagings = this.allPackagings.filter(p =>
      p.price <= this.maxPrice &&
      p.name.toLowerCase().includes(lowerSearch)
    );
  }

  // Add a new packaging
  addPackaging() {
    if (this.imageFile) {
      this.packagingService.addPackaging(this.packaging, this.imageFile).subscribe(
        (response) => {
          console.log('Packaging added:', response);
          this.loadAllPackagings();  // Refresh the list of packagings
        },
        (error) => {
          console.error('Error adding packaging:', error);
        }
      );
    }
  }

  // Handle image file selection
  onFileSelected(event: any) {
    this.imageFile = event.target.files[0];
  }

  // Update an existing packaging
  updatePackaging(id: number) {
    if (this.imageFile) {
      this.packagingService.updatePackaging(id, this.packaging, this.imageFile).subscribe(
        (response) => {
          console.log('Packaging updated:', response);
          this.loadAllPackagings();  // Refresh the list of packagings
        },
        (error) => {
          console.error('Error updating packaging:', error);
        }
      );
    }
  }

  // Delete a packaging
  deletePackaging(id: number) {
    this.packagingService.deletePackaging(id).subscribe(
      () => {
        console.log('Packaging deleted');
        this.loadAllPackagings();  // Refresh the list of packagings
      },
      (error) => {
        console.error('Error deleting packaging:', error);
      }
    );
  }

  // Load all packaging items
  loadAllPackagings() {
    this.packagingService.getAllPackagings().subscribe((data: any[]) => {
      this.allPackagings = data;
      this.filterByPrice(); // Apply filter on load
    });
  }
  getFormattedCapacity(capacity: string): string {
    switch (capacity) {
      case 'I_250_ML':
        return '250mL';
      case 'II_500_ML':
        return '500mL';
      case 'III_750_ML':
        return '750mL';
      case 'IV_1_L':
        return '1L';
      default:
        return capacity;  // In case it's not one of the predefined values
    }
  }
  filtersVisible = false; // Flag to toggle visibility of filters
  toggleFilters() {
    this.filtersVisible = !this.filtersVisible;
  }

  selectedCapacityFilter: string = '';  // Holds the selected filter option
  filterByCapacity() {
    if (this.selectedCapacityFilter) {
      this.filteredPackagings = this.allPackagings.filter(p => this.getFormattedCapacity(p.capacity) === this.selectedCapacityFilter);
    } else {
      this.filteredPackagings = this.allPackagings;  // Show all if no filter is applied
    }
  }

  selectedPackaging: any = null;

  showDetails(id: number) {
    this.packagingService.retrieveById(id).subscribe(
      data => {
        this.selectedPackaging = data;
      },
      error => {
        console.error('Error fetching packaging details', error);
      }
    );
  }

  closeModal() {
    this.selectedPackaging = null;
  }

}
