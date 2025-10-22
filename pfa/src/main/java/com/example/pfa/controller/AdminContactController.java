package com.example.pfa.controller;

import com.example.pfa.entities.Contact;
import com.example.pfa.services.IContactService;
import com.example.pfa.services.AIContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/contact")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminContactController {
    
    private final IContactService contactService;
    private final AIContactService aiContactService;
    
    @GetMapping("/all")
    public ResponseEntity<List<Contact>> getAllContacts() {
        try {
            List<Contact> contacts = contactService.getAllContacts();
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @GetMapping("/ai-insights")
    public ResponseEntity<Map<String, Object>> getAIInsights() {
        try {
            List<Contact> contacts = contactService.getAllContacts();
            
            Map<String, Object> insights = new HashMap<>();
            
            // Category distribution
            Map<String, Long> categoryCount = contacts.stream()
                .collect(Collectors.groupingBy(
                    contact -> contact.getMessageCategory() != null ? contact.getMessageCategory() : "Unknown",
                    Collectors.counting()
                ));
            insights.put("categoryDistribution", categoryCount);
            
            // Recent activity (last 7 days)
            LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
            long recentContacts = contacts.stream()
                .filter(contact -> contact.getCreatedAt().isAfter(weekAgo))
                .count();
            insights.put("recentContacts", recentContacts);
            
            // Most common categories
            String topCategory = categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
            insights.put("topCategory", topCategory);
            
            return ResponseEntity.ok(insights);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        try {
            Contact contact = contactService.getContactById(id);
            if (contact != null) {
                return ResponseEntity.ok(contact);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        try {
            contactService.deleteContact(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    // New endpoint to view AI data for a specific contact
    @GetMapping("/{id}/ai-data")
    public ResponseEntity<Map<String, String>> getContactAIData(@PathVariable Long id) {
        try {
            Contact contact = contactService.getContactById(id);
            if (contact != null) {
                Map<String, String> aiData = new HashMap<>();
                aiData.put("aiSummary", contact.getAiSummary());
                aiData.put("suggestedReply", contact.getSuggestedReply());
                aiData.put("messageCategory", contact.getMessageCategory());
                aiData.put("originalMessage", contact.getComments());
                aiData.put("customerName", contact.getName());
                aiData.put("customerEmail", contact.getEmail());
                aiData.put("subject", contact.getSubject());
                
                return ResponseEntity.ok(aiData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    // New endpoint to check AI API key status
    @GetMapping("/ai-status")
    public ResponseEntity<Map<String, String>> getAIStatus() {
        try {
            Map<String, String> status = new HashMap<>();
            status.put("apiKeyStatus", aiContactService.getApiKeyStatus());
            status.put("aiMode", aiContactService.getApiKeyStatus().contains("configured") ? "OpenRouter API" : "Enhanced Fallback Mode");
            status.put("apiTest", aiContactService.testApiKey());
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    // Test endpoint to see AI processing
    @GetMapping("/test-ai/{message}")
    public ResponseEntity<Map<String, String>> testAI(@PathVariable String message) {
        try {
            Map<String, String> result = aiContactService.processContactMessage("Test User", "Test Subject", message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}