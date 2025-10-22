package com.example.pfa.services;

import com.example.pfa.entities.Contact;
import com.example.pfa.repositories.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements IContactService {
    
    private final ContactRepository contactRepository;
    private final JavaMailSender mailSender; // Add email sender
    private final AIContactService aiContactService; // Add AI service
    
    @Override
    public Contact saveContact(Contact contact) {
        // Process with AI first
        Map<String, String> aiResults = aiContactService.processContactMessage(
            contact.getName(), 
            contact.getSubject(), 
            contact.getComments()
        );
        
        // Set AI-generated fields
        contact.setAiSummary(aiResults.get("summary"));
        contact.setSuggestedReply(aiResults.get("suggestedReply"));
        contact.setMessageCategory(aiResults.get("category"));
        
        // Save to database with AI data
        Contact savedContact = contactRepository.save(contact);
        
        // Send enhanced email notification with AI insights
        sendEnhancedEmailNotification(savedContact);
        
        return savedContact;
    }
    
    private void sendEnhancedEmailNotification(Contact contact) {
        try {
            // Enhanced email to business owner with AI insights
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("esrazitouni99@gmail.com"); // Your business email
            message.setSubject("🤖 AI-Enhanced Contact: " + contact.getSubject() + " [" + contact.getMessageCategory() + "]");
            message.setText(
                "🤖 AI-ENHANCED CONTACT FORM SUBMISSION\n" +
                "=====================================\n\n" +
                "📋 BASIC INFO:\n" +
                "Name: " + contact.getName() + "\n" +
                "Email: " + contact.getEmail() + "\n" +
                "Subject: " + contact.getSubject() + "\n" +
                "Category: " + contact.getMessageCategory() + "\n" +
                "Submitted: " + contact.getCreatedAt() + "\n\n" +
                
                "📝 ORIGINAL MESSAGE:\n" +
                contact.getComments() + "\n\n" +
                
                "🧠 AI SUMMARY:\n" +
                contact.getAiSummary() + "\n\n" +
                
                "💡 SUGGESTED REPLY:\n" +
                contact.getSuggestedReply() + "\n\n" +
                
                "⚡ QUICK ACTIONS:\n" +
                "- Reply directly to: " + contact.getEmail() + "\n" +
                "- Category: " + contact.getMessageCategory() + "\n" +
                "- Priority: " + getPriorityLevel(contact.getMessageCategory())
            );
            
            mailSender.send(message);
            
            // Send confirmation email to user
            sendConfirmationEmail(contact);
            
        } catch (Exception e) {
            // Log error but don't fail the contact save
            System.err.println("Failed to send enhanced email notification: " + e.getMessage());
        }
    }
    
    private String getPriorityLevel(String category) {
        switch (category) {
            case "Complaint": return "🔴 HIGH";
            case "Support Request": return "🟡 MEDIUM";
            case "Pricing Inquiry": return "🟡 MEDIUM";
            case "Business Partnership": return "🟢 LOW";
            case "Thank You": return "🟢 LOW";
            default: return "🟡 MEDIUM";
        }
    }
    
    private void sendConfirmationEmail(Contact contact) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(contact.getEmail());
            message.setSubject("🤖 Thank you for contacting us! [" + contact.getMessageCategory() + "]");
            
            // Use AI-generated reply as the confirmation email
            String emailBody = contact.getSuggestedReply();
            if (emailBody == null || emailBody.isEmpty()) {
                // Fallback if AI didn't generate a reply
                emailBody = "Dear " + contact.getName() + ",\n\n" +
                    "Thank you for reaching out to us. We have received your message:\n\n" +
                    "Subject: " + contact.getSubject() + "\n" +
                    "Message: " + contact.getComments() + "\n\n" +
                    "We will get back to you within 24 hours.\n\n" +
                    "Best regards,\n" +
                    "Your Business Team";
            }
            
            message.setText(emailBody);
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