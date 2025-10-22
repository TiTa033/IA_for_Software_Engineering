package com.example.pfa.services;

import com.example.pfa.entities.Contact;
import com.example.pfa.repositories.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements IContactService {
    
    private final ContactRepository contactRepository;
    private final JavaMailSender mailSender; // Add email sender
    
    @Override
    public Contact saveContact(Contact contact) {
        // Save to database
        Contact savedContact = contactRepository.save(contact);
        
        // Send email notification
        sendEmailNotification(savedContact);
        
        return savedContact;
    }
    
    private void sendEmailNotification(Contact contact) {
        try {
            // Email to business owner
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("esrazitouni99@gmail.com"); // Your business email
            message.setSubject("New Contact Form Submission: " + contact.getSubject());
            message.setText(
                "You have received a new contact form submission:\n\n" +
                "Name: " + contact.getName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Subject: " + contact.getSubject() + "\n" +
                "Message: " + contact.getComments() + "\n\n" +
                "Submitted on: " + contact.getCreatedAt()
            );
            
            mailSender.send(message);
            
            // Send confirmation email to user
            sendConfirmationEmail(contact);
            
        } catch (Exception e) {
            // Log error but don't fail the contact save
            System.err.println("Failed to send email notification: " + e.getMessage());
        }
    }
    
    private void sendConfirmationEmail(Contact contact) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(contact.getEmail());
            message.setSubject("Thank you for contacting us!");
            message.setText(
                "Dear " + contact.getName() + ",\n\n" +
                "Thank you for reaching out to us. We have received your message:\n\n" +
                "Subject: " + contact.getSubject() + "\n" +
                "Message: " + contact.getComments() + "\n\n" +
                "We will get back to you within 24 hours.\n\n" +
                "Best regards,\n" +
                "Your Business Team"
            );
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }
    
    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    
    @Override
    public Contact getContactById(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);
        return contact.orElse(null);
    }
    
    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}