🤖 AI-Assisted Contact Form Automation (Spring Boot + OpenRouter)
📌 Overview

This module extends the olive oil packaging sustainability web application by integrating an AI-powered assistant into the “Contact Us” page.
The goal is to automate message handling using artificial intelligence — reducing administrative workload and improving response time.

When a user sends a message through the contact form, the system automatically:

🧠 Summarizes the message content.

💬 Generates a polite and professional reply suggestion.

💾 Stores both summary and suggested reply in the database for the admin to review.

🏗️ Project Context

This AI module is part of a larger project that predicts the Global Warming Potential (GWP) of olive oil packaging using XGBoost.
While the main model focuses on sustainability analytics, this “Contact Us” enhancement showcases how AI can assist communication and automation within the same ecosystem.

⚙️ Technical Stack

Backend: Spring Boot (Java 17+)

Frontend: Angular (for UI integration)

AI Provider: OpenRouter API

Model Used: gpt-4o-mini

HTTP Client: Spring WebClient

Database: MySQL / PostgreSQL

Build Tool: Maven

🧩 Architecture
User → Angular Contact Form → Spring Boot Controller → AIContactService
    → OpenRouter API (GPT-4o-mini) → Summarized + Suggested Reply → Database


ContactController — Receives and processes messages from the frontend.

AIContactService — Handles API calls to OpenRouter using WebClient.

Contact Entity — Stores message, summary, and AI-generated suggestion.

ContactRepository — Persists data to the database.

🚀 Integration Steps
1️⃣. Configure API Key

Add your OpenRouter API key inside your application.properties:

openrouter.api.key=sk-your-openrouter-key

2️⃣. Create the AIContactService
@Service
public class AIContactService {
    private final WebClient webClient;
    @Value("${openrouter.api.key}") private String apiKey;
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

    public AIContactService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Map<String, String> getAISummaryAndReply(String userMessage) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Map[]{ Map.of("role", "user", "content", userMessage) }
        );

        try {
            Map<String, Object> response = webClient.post()
                    .uri(OPENROUTER_URL)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            Map<String, Object> messageObj =
                    ((Map<String, Object>) ((Map<String, Object>)
                            ((java.util.List<Object>) response.get("choices")).get(0)).get("message"));

            String content = (String) messageObj.get("content");

            return Map.of("summary", content, "reply", content);

        } catch (Exception e) {
            return Map.of(
                    "summary", "Erreur de communication avec l'API IA",
                    "reply", "Impossible de générer une réponse automatique pour le moment."
            );
        }
    }
}

💬 Example API Response

Input (user message):

"Bonjour, je souhaite connaître les options d’emballages durables pour les bouteilles de 1L."

AI Output:

{
  "summary": "L’utilisateur demande des informations sur les emballages durables pour les bouteilles de 1L.",
  "reply": "Merci pour votre message ! Nous proposons plusieurs options d’emballages durables en verre et PET recyclé. Un membre de notre équipe vous contactera bientôt avec plus de détails."
}

🧠 Prompt Design

To guide the AI effectively, structured French prompts were used:

Rôle clair du modèle : “Tu es un assistant spécialisé en communication durable.”

Objectif précis : “Résume le message et propose une réponse polie et professionnelle.”

Format attendu : JSON { "summary": ..., "reply": ... }

These prompt-engineering techniques ensure consistency and relevance in the model’s output.

🔒 Security

The API key is stored in application.properties and never exposed in the frontend.

For production, store it in environment variables or a secrets manager.

🧾 Results

✅ Automatic summarization and reply generation.
✅ Faster response handling by administrators.
✅ Seamless integration with existing Spring Boot architecture.
✅ Demonstrates applied AI in a real-world sustainability context.

🧩 Future Enhancements

Add multi-language support (English/French summary).

Include sentiment analysis to detect tone.

Implement email automation to send AI-generated replies directly.
