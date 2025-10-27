import { Component, OnInit } from '@angular/core';
import { ChatService, ChatResponse } from '../services/chat.service';

export interface Message {
  text: string;
  from: 'user' | 'bot';
  timestamp: Date;
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  messages: Message[] = [];
  input = '';
  loading = false;
  isMinimized = false;

  constructor(private chatService: ChatService) {}

  ngOnInit() {
    // Add welcome message
    this.messages.push({
      text: "Hello! I'm your AI assistant for the olive oil shop. How can I help you today?",
      from: 'bot',
      timestamp: new Date()
    });
  }

  send() {
    if (!this.input.trim() || this.loading) return;
    
    const userText = this.input.trim();
    this.messages.push({
      text: userText,
      from: 'user',
      timestamp: new Date()
    });
    
    this.input = '';
    this.loading = true;

    this.chatService.sendMessage(userText).subscribe({
      next: (response: ChatResponse) => {
        this.messages.push({
          text: response.reply,
          from: 'bot',
          timestamp: new Date()
        });
        this.loading = false;
        this.scrollToBottom();
      },
      error: (error) => {
        console.error('Chat error:', error);
        this.messages.push({
          text: 'Sorry, I encountered an error. Please try again later.',
          from: 'bot',
          timestamp: new Date()
        });
        this.loading = false;
        this.scrollToBottom();
      }
    });
  }

  onKeyPress(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.send();
    }
  }

  toggleMinimize() {
    this.isMinimized = !this.isMinimized;
  }

  private scrollToBottom() {
    setTimeout(() => {
      const chatWindow = document.querySelector('.chat-messages');
      if (chatWindow) {
        chatWindow.scrollTop = chatWindow.scrollHeight;
      }
    }, 100);
  }

  formatTime(timestamp: Date): string {
    return timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }
}
