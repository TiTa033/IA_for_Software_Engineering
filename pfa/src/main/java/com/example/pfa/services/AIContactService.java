package com.example.pfa.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AIContactService {
    
    @Value("${ai.openrouter.api.key:}")
    private String openRouterApiKey;
    
    @Value("${ai.openrouter.model:gpt-4o-mini}")
    private String openRouterModel;
    
    @Value("${ai.openrouter.base-url:https://openrouter.ai/api/v1}")
    private String openRouterBaseUrl;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public AIContactService() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }
    
    // Method to check AI service status
    public String getApiKeyStatus() {
        if (openRouterApiKey == null || openRouterApiKey.trim().isEmpty() || 
            openRouterApiKey.equals("your_openrouter_api_key_here")) {
            return "OpenRouter API key not configured - Using Enhanced Fallback Mode";
        } else {
            return "OpenRouter API key configured (length: " + openRouterApiKey.length() + ")";
        }
    }
    
    // Method to test AI processing
    public String testApiKey() {
        if (openRouterApiKey == null || openRouterApiKey.trim().isEmpty() || 
            openRouterApiKey.equals("your_openrouter_api_key_here")) {
            return "AI Test Result: FALLBACK - No API key configured";
        }
        
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", openRouterModel);
            requestBody.put("messages", List.of(
                Map.of("role", "user", "content", "Hello! This is a test message.")
            ));
            requestBody.put("max_tokens", 50);
            
            String response = webClient.post()
                .uri(openRouterBaseUrl + "/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openRouterApiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("HTTP-Referer", "http://localhost:8089")
                .header("X-Title", "PFA Contact Form")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            return "AI Test Result: SUCCESS - OpenRouter API working";
            
        } catch (Exception e) {
            return "AI Test Result: ERROR - " + e.getMessage();
        }
    }
    
    public Map<String, String> processContactMessage(String name, String subject, String comments) {
        Map<String, String> aiResults = new HashMap<>();
        
        try {
            // Validate inputs
            if (name == null) name = "Unknown";
            if (subject == null) subject = "No Subject";
            if (comments == null) comments = "No Message";
            
            // Try OpenRouter API first
            if (openRouterApiKey != null && !openRouterApiKey.trim().isEmpty() && 
                !openRouterApiKey.equals("your_openrouter_api_key_here")) {
                
                try {
                    Map<String, String> openRouterResults = processWithOpenRouter(name, subject, comments);
                    if (openRouterResults != null && !openRouterResults.isEmpty()) {
                        return openRouterResults;
                    }
                } catch (Exception e) {
                    log.warn("OpenRouter API failed, using fallback: {}", e.getMessage());
                }
            }
            
            // Fallback to enhanced local AI
            return processWithFallbackAI(name, subject, comments);
            
        } catch (Exception e) {
            log.error("Error processing contact message with AI: {}", e.getMessage());
            // Fallback values if AI fails
            aiResults.put("summary", "Message received from " + name + " regarding " + subject);
            aiResults.put("suggestedReply", "Thank you for your message. We will get back to you soon.");
            aiResults.put("category", "General Inquiry");
            return aiResults;
        }
    }
    
    private Map<String, String> processWithOpenRouter(String name, String subject, String comments) {
        log.info("Using OpenRouter API for AI processing");
        
        String fullMessage = String.format("Name: %s\nSubject: %s\nMessage: %s", name, subject, comments);
        
        // Create the prompt for AI processing
        String prompt = String.format("""
            You are an AI assistant helping with customer service. Please analyze this contact form submission and provide:
            
            1. A brief summary (max 100 characters)
            2. A suggested reply (professional, helpful tone)
            3. A category (Complaint, Support Request, Pricing Inquiry, Business Partnership, Thank You, General Inquiry)
            
            Contact Form Submission:
            %s
            
            Please respond in this exact JSON format:
            {
                "summary": "Brief summary here",
                "suggestedReply": "Dear %s,\\n\\n[Your suggested reply here]\\n\\nBest regards,\\nCustomer Service Team",
                "category": "Category Name"
            }
            """, fullMessage, name);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", openRouterModel);
        requestBody.put("messages", List.of(
            Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);
        
        String response = webClient.post()
            .uri(openRouterBaseUrl + "/chat/completions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + openRouterApiKey)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header("HTTP-Referer", "http://localhost:8089")
            .header("X-Title", "PFA Contact Form")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        log.info("OpenRouter API response: {}", response);
        
        // Parse the response
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("choices") && jsonNode.get("choices").isArray() && 
                jsonNode.get("choices").size() > 0) {
                
                String content = jsonNode.get("choices").get(0).get("message").get("content").asText();
                
                // Try to parse JSON from the response
                try {
                    JsonNode aiResponse = objectMapper.readTree(content);
                    Map<String, String> results = new HashMap<>();
                    results.put("summary", aiResponse.get("summary").asText());
                    results.put("suggestedReply", aiResponse.get("suggestedReply").asText());
                    results.put("category", aiResponse.get("category").asText());
                    
                    log.info("Successfully processed with OpenRouter API");
                    return results;
                    
                } catch (Exception e) {
                    log.warn("Failed to parse AI response as JSON, using fallback");
                    return null;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse OpenRouter response: {}", e.getMessage());
            return null;
        }
        
        return null;
    }
    
    private Map<String, String> processWithFallbackAI(String name, String subject, String comments) {
        log.info("Using enhanced fallback AI processing");
        
        Map<String, String> aiResults = new HashMap<>();
        
        // Combine all text for analysis
        String fullMessage = String.format("Name: %s\nSubject: %s\nMessage: %s", name, subject, comments);
        
        // Generate AI summary
        String summary = generateSummary(fullMessage);
        aiResults.put("summary", summary);
        
        // Generate suggested reply
        String suggestedReply = generateSuggestedReply(name, subject, comments);
        aiResults.put("suggestedReply", suggestedReply);
        
        // Categorize the message
        String category = categorizeMessage(subject, comments);
        aiResults.put("category", category);
        
        return aiResults;
    }
    
    private String generateSummary(String message) {
        String lowerMessage = message.toLowerCase();
        
        // Enhanced summarization with context
        if (lowerMessage.contains("unhappy") || lowerMessage.contains("angry") || lowerMessage.contains("disappointed") || 
            lowerMessage.contains("complaint") || lowerMessage.contains("problem") || lowerMessage.contains("issue")) {
            return "🔴 Complaint: Customer expressing dissatisfaction with service quality";
        } else if (lowerMessage.contains("price") || lowerMessage.contains("cost") || lowerMessage.contains("pricing") || 
                   lowerMessage.contains("expensive") || lowerMessage.contains("cheap") || lowerMessage.contains("budget")) {
            return "💰 Pricing inquiry: Customer requesting cost information";
        } else if (lowerMessage.contains("help") || lowerMessage.contains("support") || lowerMessage.contains("assistance") || 
                   lowerMessage.contains("problem") || lowerMessage.contains("bug") || lowerMessage.contains("technical")) {
            return "🛠️ Support request: Customer needs technical assistance";
        } else if (lowerMessage.contains("partnership") || lowerMessage.contains("collaborate") || lowerMessage.contains("business") || 
                   lowerMessage.contains("work together") || lowerMessage.contains("cooperation")) {
            return "🤝 Partnership inquiry: Business collaboration opportunity";
        } else if (lowerMessage.contains("thank") || lowerMessage.contains("grateful") || lowerMessage.contains("appreciate")) {
            return "🙏 Thank you message: Customer expressing gratitude";
        } else {
            return "📝 General inquiry: Customer seeking information or assistance";
        }
    }
    
    private String generateSuggestedReply(String name, String subject, String comments) {
        String lowerSubject = subject.toLowerCase();
        String lowerComments = comments.toLowerCase();
        
        // Generate contextual replies based on content
        if (lowerComments.contains("unhappy") || lowerComments.contains("angry") || lowerComments.contains("disappointed")) {
            return String.format("Dear %s,\n\nI sincerely apologize for the poor experience you've had with our service. Your feedback is extremely important to us, and I want to personally address your concerns.\n\nCould you please provide more details about what specifically went wrong? This will help us:\n• Investigate the issue thoroughly\n• Take immediate corrective action\n• Ensure this doesn't happen again\n\nI'm committed to resolving this matter to your complete satisfaction. Please reply with your specific concerns, and I'll personally oversee the resolution.\n\nThank you for bringing this to our attention.\n\nBest regards,\nCustomer Service Manager", name);
        } else if (lowerSubject.contains("price") || lowerComments.contains("price") || lowerComments.contains("cost")) {
            return String.format("Dear %s,\n\nThank you for your interest in our services! I'd be happy to provide you with detailed pricing information.\n\nOur pricing varies based on your specific needs and requirements. To give you the most accurate quote, could you please share:\n• Your project scope\n• Timeline requirements\n• Specific features needed\n• Budget range (if applicable)\n\nI'll prepare a customized proposal for you within 24 hours. You can also schedule a free consultation call to discuss your requirements in detail.\n\nLooking forward to helping you!\n\nBest regards,\nSales Team", name);
        } else if (lowerComments.contains("help") || lowerComments.contains("support") || lowerComments.contains("problem")) {
            return String.format("Dear %s,\n\nThank you for reaching out! I'm here to help resolve your issue quickly.\n\nTo provide you with the best support, please share:\n• Detailed description of the problem\n• Steps you've already tried\n• Any error messages you're seeing\n• Your account information (if applicable)\n\nI'll investigate this immediately and get back to you with a solution within 2-4 hours during business hours.\n\nIf this is urgent, please call our support hotline at [phone number] for immediate assistance.\n\nBest regards,\nSupport Team", name);
        } else if (lowerComments.contains("partnership") || lowerComments.contains("collaborate")) {
            return String.format("Dear %s,\n\nThank you for your interest in partnering with us! We're always excited about potential collaboration opportunities.\n\nTo better understand how we can work together, could you please share:\n• Your company background\n• Partnership proposal details\n• Expected collaboration scope\n• Timeline for partnership\n\nI'll review your proposal and connect you with our business development team. We typically respond to partnership inquiries within 48 hours.\n\nLooking forward to exploring this opportunity!\n\nBest regards,\nBusiness Development Team", name);
        } else {
            return String.format("Dear %s,\n\nThank you for reaching out to us! We have received your message and appreciate you taking the time to contact us.\n\nYour message regarding \"%s\" has been forwarded to the appropriate team, and we will get back to you within 24 hours with a detailed response.\n\nIf you have any urgent questions, please don't hesitate to call us directly.\n\nThank you for choosing our services!\n\nBest regards,\nCustomer Service Team", name, subject);
        }
    }
    
    private String categorizeMessage(String subject, String comments) {
        String lowerSubject = subject.toLowerCase();
        String lowerComments = comments.toLowerCase();
        
        // Prioritize complaint detection first
        if (lowerComments.contains("unhappy") || lowerComments.contains("angry") || lowerComments.contains("disappointed") || 
            lowerComments.contains("complaint") || lowerComments.contains("terrible") || lowerComments.contains("awful") ||
            lowerComments.contains("hate") || lowerComments.contains("worst") || lowerComments.contains("horrible")) {
            return "Complaint";
        } else if (lowerSubject.contains("price") || lowerComments.contains("price") || lowerComments.contains("cost") || 
                   lowerComments.contains("pricing") || lowerComments.contains("expensive") || lowerComments.contains("cheap")) {
            return "Pricing Inquiry";
        } else if (lowerComments.contains("help") || lowerComments.contains("support") || lowerComments.contains("assistance") || 
                   lowerComments.contains("problem") || lowerComments.contains("bug") || lowerComments.contains("technical") ||
                   lowerComments.contains("issue") || lowerComments.contains("error")) {
            return "Support Request";
        } else if (lowerComments.contains("partnership") || lowerComments.contains("collaborate") || lowerComments.contains("business") || 
                   lowerComments.contains("work together") || lowerComments.contains("cooperation")) {
            return "Business Partnership";
        } else if (lowerComments.contains("thank") || lowerComments.contains("grateful") || lowerComments.contains("appreciate")) {
            return "Thank You";
        } else {
            return "General Inquiry";
        }
    }
}