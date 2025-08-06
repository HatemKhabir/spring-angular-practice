import { Component, input, InputSignal, output } from '@angular/core';
import { ModelChatResponseModel, ModelUserResponseModel } from '../../api/models';
import { DatePipe } from '@angular/common';
import { ApiUse4RService } from '../../api/services';

@Component({
  selector: 'app-chat-list',
  imports: [DatePipe],
  templateUrl: './chat-list.component.html',
  styleUrl: './chat-list.component.scss',
})
export class ChatListComponent {

  chats: InputSignal<ModelChatResponseModel[]> = input<
    ModelChatResponseModel[]
  >([]);
  searchNewContact = false;
  contacts:Array<ModelUserResponseModel> = [];
  chatSelected=output<ModelChatResponseModel>();

  constructor(
    private userService: ApiUse4RService,
  ) {
  }

  searchContacts() {
    this.userService.getAllUsers().subscribe({
      next: (response) => {
        this.contacts = response;
        this.searchNewContact = true;
        console.log('Contacts fetched successfully:', this.contacts);
      },
      error: (error) => {
        console.error('Error fetching contacts:', error);
      },
    });
  }
  wrapMessage(lastMessage: string | undefined): string {
    if (lastMessage && lastMessage.length <= 20) {
      return lastMessage;
    }
    return lastMessage?.substring(0, 20) + '...';
  }
  contactClicked(_t33: ModelUserResponseModel) {
    throw new Error('Method not implemented.');
    }
    addContact(_t33: ModelUserResponseModel) {
    throw new Error('Method not implemented.');
    }
  chatClicked(chat: ModelChatResponseModel) {
  this.chatSelected.emit(chat);
  }
  searchContact() {
    throw new Error('Method not implemented.');
  }
}
