import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;

  constructor() {}

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
        url: 'http://localhost:9090',
        realm: 'whatsapp-clone',
        clientId: 'whatsapp-clone-ui',
      });
    }
    return this._keycloak;
  }

  async init() {
    const authenticated = await this.keycloak.init({
      onLoad: 'login-required',
    });
  }
  async login() {
    return this._keycloak?.login();
  }
  async logout() {
    return this._keycloak?.logout({redirectUri:'http://localhost:4200'});
  }
  async userId() {
    return this._keycloak?.tokenParsed?.sub as string;
  }
  get token() {
    return this._keycloak?.token;
  }
  get isTokenExpired() {
    return this._keycloak?.isTokenExpired();
  }
  get username() {
    return this._keycloak?.tokenParsed?.['name'] as string;
  }

  accountManagement() {
    return this._keycloak?.accountManagement();
  }

}
