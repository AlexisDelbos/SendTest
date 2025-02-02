import { Injectable } from '@angular/core';
import { User } from '../model/user.model';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticateService {
  userConnected: User = new User(0, "", "", { id: 0, role: "" });

  constructor(private apiService: ApiService) { }

  /**
   * Méthode qui renvoi un utilisateur en locale storage, s'il est trouvé c'est qu'il est connecté !
   * @returns user s'il existe
   */
  getUser() {
    let user = localStorage.getItem('user');
    if (user) {
      try {
        this.userConnected = JSON.parse(atob(user));
      } catch (e) {
        console.error("Erreur lors du décryptage ou parsing :", e);
      }
    }
    return this.userConnected;
  }

  login(username: string, password: string): void {
    this.apiService.login(username, password).subscribe(response => {
      if (response && response['access-token']) {
        this.setToken(response['access-token']); 
      }
    });
  }

  setToken(token: string) {
    localStorage.setItem('access-token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('access-token');
  }


  isConnected() {
    return localStorage.getItem('user') != null;
  }

  disconnected() {
    localStorage.removeItem('user');
    this.userConnected = new User(0, "", "", { id: 0, role: "" });
  }

  isAdmin() {
    let user = this.getUser();
    return user && user.role && user.role.role === 'ADMIN';
  }

  isUser() {
    let user = this.getUser();
    return user && user.role && user.role.role === 'USER';
  }

  setUser(user: User): any {
    user.password = '';
    this.userConnected = user;
    localStorage.setItem('user', btoa(JSON.stringify(user)));  //cryptage des données avant stockage en LS
  }
}
