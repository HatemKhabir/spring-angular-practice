import { Component, OnInit } from '@angular/core';
import { ChatListComponent } from "../../components/chat-list/chat-list.component";
import { ModelChatResponseModel } from '../../api/models';
import { ApiChatService } from '../../api/services';

@Component({
  selector: 'app-main',
  imports: [ChatListComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent implements OnInit {

chats:Array<ModelChatResponseModel> = [];

  constructor(
    private chatService:ApiChatService
  ) { }

  ngOnInit(): void {
this.getAllChats();
  }

  private getAllChats(){
    this.chatService.getChatsByReceiver().subscribe({
      next: (response) => {
        this.chats = response;
      },
      error: (error) => {
        console.error('Error fetching chats', error);
      }
    })
  }
}
