/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

export interface ModelMessageResponseModel {
  content?: string;
  createdAt?: string;
  id?: number;
  media?: Array<string>;
  receiverId?: string;
  senderId?: string;
  state?: 'SENT' | 'SEEN';
  type?: 'TEXT' | 'IMAGE' | 'AUDIO' | 'VIDEO';
}
