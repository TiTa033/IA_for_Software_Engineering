# Landing Chat App

Angular 16 app with a simple chatbot that supports two modes:

- **Static mode**: answers from a local JSON file.
- **Dynamic mode**: answers via an API proxy to OpenAI (chat-proxy).

## Requirements

- Node.js 18+
- Angular CLI 16+

## Quick start

1. Install dependencies
   - Frontend: `npm install`
   - Chat proxy: `cd chat-proxy` then `npm install`
2. Choose chat mode (see below) and run the app.

## Chat modes

### Static mode (default)

- Config: `src/environments/environment.ts`
  - `chatStatic: true`
  - `chatStaticDataPath: 'assets/chat/static-responses.json'`
- Data file: `src/assets/chat/static-responses.json`
- Start frontend: `npm start` (or `ng serve`) and open http://localhost:4200

Update `static-responses.json` to add new patterns/responses. Keep patterns lowercase, punctuation-free phrases for best matching.

### Dynamic mode (OpenAI via proxy)

1. Set `chatStatic: false` in `src/environments/environment.ts` (or use a production env that omits it).
2. Configure and run the chat proxy:
   - File: `chat-proxy/.env`
     - `OPENAI_API_KEY=...` (required)
     - `PORT=3001` (optional, defaults to 3001)
     - `RATE_LIMIT_MS=15000` (optional)
   - Start proxy: from `chat-proxy/` run `npm start`
3. Start frontend: `npm start` then chat normally.

The frontend calls `/ai/chat` which is served by the proxy in dynamic mode.

## Development scripts

- `npm start` – run the Angular dev server at http://localhost:4200
- `ng build` – build the app to `dist/`
- `ng test` – run unit tests (Karma)

## Editing static responses

```json
[
  { "patterns": ["hello", "hi", "hey"], "response": "Hello! I’m your assistant. How can I help you today?" },
  { "patterns": ["who are you", "what are you"], "response": "I’m your virtual assistant here to help with questions and tasks." }
]
```

Tips:
- Keep patterns lowercase, no punctuation.
- Add common phrasing variants (e.g., `how are you`, `how is it going`).
- The service uses normalization, delimiter splitting, and word-boundary matching.

## Troubleshooting

- Bot replies "the server is loaded": no pattern matched.
  - Check your input and patterns; avoid punctuation in patterns.
  - In dynamic mode, ensure the proxy is running and `.env` is set.
- CORS errors: make sure the proxy and frontend are on allowed origins.

## Project structure

- `src/app/services/chat.service.ts` – chat logic, static vs dynamic routing.
- `src/assets/chat/static-responses.json` – static intents and replies.
- `chat-proxy/` – minimal Express proxy to OpenAI with dotenv config.

## Angular CLI

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 16.2.5.
