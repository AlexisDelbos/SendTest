import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Training } from 'src/app/model/training.model';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})

/**
 * Composant de gestion d'un panier permettant l'affichage du panier, la suppression éventuelle de formations et le passage à l'étape suivante
 */
export class CartComponent implements OnInit {
  cart : Training[] | undefined;
  empty_cart : boolean = false;
  amount : number = 0;
  error : string | undefined;
  constructor(private cartService : CartService , private router : Router, private authService : AuthenticateService) { }

  ngOnInit(): void {
    this.amount = this.cartService.getAmount();
    this.initCart();
  }

  initCart(){
    this.cart = this.cartService.getCart();
    this.empty_cart = (this.cart.length > 0) ? false : true;
  }

  onRemoveFromCart(training : Training){
    this.cartService.removeTraining(training);
    this.initCart();
  }

  onNewOrder(){
    if(this.cartService.getSize() > 0) {
      if(this.authService.isConnected()) {
        this.router.navigateByUrl('customer');  
      }
      else this.error = 'vous devez être connecté pour passer commande';
    }
    else this.error = 'panier vide';
  }
}
