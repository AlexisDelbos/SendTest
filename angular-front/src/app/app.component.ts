import { Component } from '@angular/core';
import { AuthenticateService } from './services/authenticate.service';
import { CartService } from './services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'trainings-front-app';
  showNav: boolean = true;

  constructor(public cartService : CartService, public authService : AuthenticateService,private router: Router){
    this.router.events.subscribe(() => {
      this.showNav = this.router.url !== '/login'; // Cache la nav pour /login
    });
  }
  onLogin(){
  }
}


