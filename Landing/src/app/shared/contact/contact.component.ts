import { Component } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ContactService, Contact } from '../../services/contact.service';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent {

  contactForm!: UntypedFormGroup;
  errormessage: any = " Please enter a name*";
  isLoading = false;
  successMessage = '';

  constructor(
    private formBuilder: UntypedFormBuilder,
    private contactService: ContactService
  ) { }

  ngOnInit() {

    this.contactForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      subject: ['', [Validators.required]],
      comments: ['', [Validators.required]]
    });

  }

  validateForm(): boolean {
    if (this.contactForm.invalid) {
      this.markFormGroupTouched();
      return false;
    }
    return true;
  }

  private markFormGroupTouched() {
    Object.keys(this.contactForm.controls).forEach(key => {
      const control = this.contactForm.get(key);
      control?.markAsTouched();
    });
  }

  sendMsg() {
    if (!this.validateForm()) {
      return;
    }

    this.isLoading = true;
    this.successMessage = '';
    this.clearErrorMessages();

    const contactData: Contact = {
      name: this.contactForm.get('name')?.value,
      email: this.contactForm.get('email')?.value,
      subject: this.contactForm.get('subject')?.value,
      comments: this.contactForm.get('comments')?.value
    };

    this.contactService.submitContact(contactData).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = 'Thank you for your message! We will get back to you soon.';
        this.contactForm.reset();
        this.showSuccessMessage();
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error submitting contact form:', error);
        this.showErrorMessage('Sorry, there was an error sending your message. Please try again later.');
      }
    });
  }

  private clearErrorMessages() {
    const errorElement = document.getElementById('error-msg');
    if (errorElement) {
      errorElement.innerHTML = '';
    }
  }

  private showSuccessMessage() {
    const errorElement = document.getElementById('error-msg');
    if (errorElement) {
      errorElement.innerHTML = `<div class='alert alert-success error_message'><i data-feather='check-circle' class='icon-sm align-middle me-2'></i>${this.successMessage}</div>`;
    }
  }

  private showErrorMessage(message: string) {
    const errorElement = document.getElementById('error-msg');
    if (errorElement) {
      errorElement.innerHTML = `<div class='alert alert-danger error_message'><i data-feather='alert-circle' class='icon-sm align-middle me-2'></i>${message}</div>`;
    }
  }

  // Helper method to check if a field has errors
  hasError(fieldName: string): boolean {
    const field = this.contactForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  // Helper method to get error message for a field
  getErrorMessage(fieldName: string): string {
    const field = this.contactForm.get(fieldName);
    if (field?.errors?.['required']) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
    }
    if (field?.errors?.['email']) {
      return 'Please enter a valid email address';
    }
    return '';
  }
}