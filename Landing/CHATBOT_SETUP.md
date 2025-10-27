# AI Chatbot Assistant Setup Guide

## Overview
This guide will help you set up the AI Chatbot Assistant for your Angular + Spring Boot olive oil shop application.

## Features
- 🤖 AI-powered chatbot with OpenAI integration
- 💬 Beautiful chat UI with minimize/maximize functionality
- 🔄 Real-time messaging with typing indicators
- 📱 Responsive design for mobile and desktop
- 🎨 Modern gradient design with smooth animations

## Prerequisites

### 1. OpenAI API Key
- Go to [OpenAI Platform](https://platform.openai.com/)
- Create an account and generate an API key
- Copy your API key (starts with `sk-`)

### 2. Environment Setup
Make sure you have:
- Java 17+ installed
- Node.js 18+ installed
- Angular CLI installed
- Maven installed

## Backend Setup (Spring Boot)

### 1. Set Environment Variable
Set your OpenAI API key as an environment variable:

**Windows (PowerShell):**
```powershell
$env:OPENAI_API_KEY="sk-your-api-key-here"
```

**Windows (Command Prompt):**
```cmd
set OPENAI_API_KEY=sk-your-api-key-here
```

**Linux/macOS:**
```bash
export OPENAI_API_KEY="sk-your-api-key-here"
```

### 2. Start Spring Boot Backend
```bash
cd Back/pfa
mvn spring-boot:run
```

The backend will start on `http://localhost:8089/pfa`

## Frontend Setup (Angular)

### 1. Install Dependencies
```bash
cd Front/Landing
npm install
```

### 2. Start Angular Development Server
```bash
ng serve
```

The frontend will start on `http://localhost:4200`

## Configuration

### Backend Configuration
The backend is already configured with:
- **Port**: 8089
- **Context Path**: /pfa
- **CORS**: Enabled for localhost:4200
- **OpenAI Model**: gpt-3.5-turbo (configurable in application.properties)

### Frontend Configuration
The frontend is configured with:
- **Proxy**: Routes `/api/*` to `http://localhost:8089/pfa`
- **Chat Component**: Available on all pages
- **Responsive Design**: Works on mobile and desktop

## API Endpoints

### Chat Endpoint
- **URL**: `POST /api/chat`
- **Request Body**:
  ```json
  {
    "message": "What are the benefits of olive oil?"
  }
  ```
- **Response**:
  ```json
  {
    "reply": "Olive oil has many health benefits including..."
  }
  ```

## Usage

### 1. Access the Application
- Open your browser and go to `http://localhost:4200`
- You'll see the chat widget in the bottom-right corner

### 2. Start Chatting
- Click on the chat widget to open it
- Type your message and press Enter or click Send
- The AI will respond with helpful information about olive oils

### 3. Chat Features
- **Minimize/Maximize**: Click the header to toggle
- **Typing Indicator**: Shows when AI is thinking
- **Message History**: All messages are saved in the session
- **Responsive**: Works on all screen sizes

## Customization

### 1. Change AI Personality
Edit `Back/pfa/src/main/java/com/example/pfa/services/ChatService.java`:
```java
messages.add(Map.of("role", "system", "content", 
    "Your custom system prompt here..."));
```

### 2. Change OpenAI Model
Edit `Back/pfa/src/main/resources/application.properties`:
```properties
openai.model=gpt-4  # or any other model
```

### 3. Customize Chat UI
Edit `Front/Landing/src/app/chat/chat.component.scss` to change colors, fonts, etc.

## Troubleshooting

### Common Issues

#### 1. "API key not found" Error
- Make sure you've set the `OPENAI_API_KEY` environment variable
- Restart your Spring Boot application after setting the variable

#### 2. CORS Errors
- The backend already has CORS configured for localhost:4200
- If you change ports, update the CORS configuration

#### 3. Proxy Errors
- Make sure the Spring Boot backend is running on port 8089
- Check that the proxy configuration in `proxy.conf.json` is correct

#### 4. Chat Widget Not Appearing
- Make sure you've imported the `ChatComponent` in `app.module.ts`
- Check the browser console for any JavaScript errors

### Debug Steps

1. **Check Backend Logs**: Look for any errors in the Spring Boot console
2. **Check Frontend Console**: Open browser dev tools and check for errors
3. **Test API Directly**: Use Postman to test the `/api/chat` endpoint
4. **Check Environment Variables**: Verify the API key is set correctly

## Security Notes

- ✅ API key is stored on the backend (secure)
- ✅ No sensitive data in frontend code
- ✅ CORS properly configured
- ✅ Input validation on backend

## Production Deployment

### Environment Variables
Set these in your production environment:
```
OPENAI_API_KEY=your-production-api-key
```

### Configuration Updates
- Update CORS origins for production domain
- Consider rate limiting for API calls
- Add logging and monitoring

## Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Verify all prerequisites are installed
3. Check the console logs for errors
4. Ensure the API key is valid and has credits

## Next Steps

- Add more sophisticated conversation memory
- Implement user authentication for chat history
- Add file upload capabilities for image analysis
- Integrate with your product database for specific recommendations
