import { Component, OnInit } from '@angular/core';
import { ChatListComponent } from '../../components/chat-list/chat-list.component';
import {
  ModelChatResponseModel,
  ModelMessageRequestModel,
  ModelMessageResponseModel,
} from '../../api/models';
import { ApiChatService, ApiMessageService } from '../../api/services';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';
import { AsyncPipe, DatePipe } from '@angular/common';
import { PickerComponent } from '@ctrl/ngx-emoji-mart';
import { FormsModule } from '@angular/forms';
import { EmojiData } from '@ctrl/ngx-emoji-mart/ngx-emoji';
import { setMessagesToSeen } from '../../api/fn/message/set-messages-to-seen';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    ChatListComponent,
    AsyncPipe,
    DatePipe,
    PickerComponent,
    FormsModule,
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent implements OnInit {
  selectedChat: ModelChatResponseModel | null = null;
  chats: Array<ModelChatResponseModel> = [];
  chatMessages: ModelMessageResponseModel[] = [];
  showEmojis = false;
  messageContent = '';
  constructor(
    private chatService: ApiChatService,
    private keyCloakService: KeycloakService,
    private messageService: ApiMessageService
  ) {}

  ngOnInit(): void {
    this.getAllChats();
  }

  private getAllChats() {
    this.chatService.getChatsByReceiver().subscribe({
      next: (response) => {
        this.chats = response;
      },
      error: (error) => {
        console.error('Error fetching chats', error);
      },
    });
  }

  logout() {
    this.keyCloakService.logout();
  }
  userProfile() {
    this.keyCloakService.accountManagement();
  }
  chatClicked(chatResponse: ModelChatResponseModel) {
    this.selectedChat = chatResponse;
    this.getChatMessages(chatResponse.id as string);
    this.setMessagesToSeen(chatResponse.id as string);
    this.selectedChat.unreadCount = 0; // Refresh chats after selection
    console.log('Selected chat:', this.selectedChat);
  }
  
  getChatMessages(chatId: string) {
    this.messageService.getMessages({ 'chat-id': chatId }).subscribe({
      next: (response) => {
        this.chatMessages = response;
        console.log('Messages for chat:', this.chatMessages);
      },
    });
  }

  async isSelfMessage(message: ModelMessageResponseModel) {
    const userId = await this.keyCloakService.userId();
    return message.senderId === userId;
  }

  uploadMedia(target: EventTarget | null) {
    throw new Error('Method not implemented.');
  }

  onSelectEmoji(emojiSelected: any) {
    const emoji: EmojiData = emojiSelected.emoji;
    this.messageContent += emoji.native; // Append the selected emoji to the message content
  }
  setMessagesToSeen(chatId: string) {
    this.messageService.setMessagesToSeen({
      'chat-id':chatId
    }).subscribe({
      next: (response) => {
        console.log('Messages set to seen:', response);
      },
      error: (error) => {
        console.error('Error setting messages to seen', error);
      },  
    })
  }
  onClick() {
this.setMessagesToSeen(this.selectedChat?.id as string);
  }
  async sendMessage() {
    if(this.messageContent){
      const messageRequest:ModelMessageRequestModel={
        chatId: this.selectedChat?.id as string,
        content: this.messageContent,
        senderId: await this.getSenderId(),
        receiverId: await this.getReceiverId(),
        type: 'TEXT',
      };
      this.messageService.saveMessage({
        body: messageRequest
      }).subscribe({
        next:()=>{
          const message:ModelMessageResponseModel={
            content: this.messageContent,
            senderId: messageRequest.senderId,
            receiverId: messageRequest.receiverId,
            type: 'TEXT',
            state: 'SENT',
            createdAt: new Date().toString(),
          };
          if (this.selectedChat) {
            this.selectedChat.lastMessage = this.messageContent;
          }
          this.chatMessages.push(message);
          this.messageContent = ''; // Clear the input field after sending
        this.showEmojis = false; // Hide the emoji picker after sending a message
        }
      })
    }
  }
  async getSenderId(): Promise<string> {
    if (this.selectedChat?.senderId=== await this.keyCloakService.userId()) {
      return this.selectedChat?.senderId as string;
    } 
    return this.selectedChat?.receiverId as string;
  }
  async getReceiverId(): Promise<string> {
    if (this.selectedChat?.senderId=== await this.keyCloakService.userId()) {
      return this.selectedChat?.receiverId as string;
    } 
    return this.selectedChat?.senderId as string;
  }
}
