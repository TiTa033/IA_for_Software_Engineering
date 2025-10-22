# Contact Form Integration - Setup and Testing Guide

## Overview
The contact form has been successfully integrated with the backend API. Here's what was implemented:

### Backend Components Created:
1. **Contact Entity** (`Contact.java`) - JPA entity for storing contact form data
2. **ContactRepository** (`ContactRepository.java`) - Spring Data JPA repository
3. **IContactService** (`IContactService.java`) - Service interface
4. **ContactServiceImpl** (`ContactServiceImpl.java`) - Service implementation
5. **ContactController** (`ContactController.java`) - REST API controller

### Frontend Components Updated:
1. **ContactService** (`contact.service.ts`) - Angular service for API communication
2. **ContactComponent** (`contact.component.ts`) - Updated with proper form handling
3. **ContactComponent HTML** (`contact.component.html`) - Enhanced with validation and loading states

## API Endpoints

### POST `/pfa/contact/submit`
Submit a contact form message
- **Request Body**: 
  ```json
  {
    "name": "John Doe",
    "email": "john@example.com", 
    "subject": "Inquiry",
    "comments": "Your message here"
  }
  ```
- **Response**: Contact object with generated ID and timestamp

### GET `/pfa/contact/all`
Get all contact messages (for admin purposes)

### GET `/pfa/contact/{id}`
Get a specific contact message by ID

### DELETE `/pfa/contact/{id}`
Delete a contact message by ID

## Testing the Integration

### Prerequisites:
1. MySQL database running on localhost:3306
2. Database `pfadb` should exist (will be created automatically if `createDatabaseIfNotExist=true`)
3. Backend running on port 8089
4. Frontend running on port 4200

### Steps to Test:

1. **Start the Backend:**
   ```bash
   cd pfa
   mvn spring-boot:run
   ```

2. **Start the Frontend:**
   ```bash
   cd Landing
   ng serve
   ```

3. **Test the Contact Form:**
   - Navigate to `http://localhost:4200`
   - Scroll to the contact section
   - Fill out the form with:
     - Name: Required field
     - Email: Required field with email validation
     - Subject: Required field
     - Comments: Required field
   - Click "Send Message"
   - You should see a success message: "Thank you for your message! We will get back to you soon."

4. **Verify Data Storage:**
   - Check your MySQL database `pfadb` table `contacts`
   - The submitted data should be stored with an auto-generated ID and timestamp

### Features Implemented:

✅ **Form Validation:**
- Required field validation
- Email format validation
- Real-time error display

✅ **User Experience:**
- Loading spinner during submission
- Success/error messages
- Form reset after successful submission
- Disabled submit button during loading

✅ **Backend Integration:**
- RESTful API endpoints
- Database persistence
- Error handling
- CORS configuration for Angular frontend

✅ **Data Model:**
- Contact entity with proper JPA annotations
- Automatic timestamp generation
- Database table creation

## Database Schema

The `contacts` table will be automatically created with the following structure:
```sql
CREATE TABLE contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    comments TEXT NOT NULL,
    created_at DATETIME
);
```

## Error Handling

The system includes comprehensive error handling:
- Frontend validation before submission
- Backend error responses
- User-friendly error messages
- Console logging for debugging

## Security Considerations

- CORS is configured for `http://localhost:4200`
- Input validation on both frontend and backend
- SQL injection protection through JPA
- No sensitive data exposure in error messages
