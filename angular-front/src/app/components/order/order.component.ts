import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {
  dateOrder : Date = new Date();  
  showModal = false;
  modalTitle = 'Commande confirmée';
  modalContent = 'Votre commande a bien été prise en compte';
  modalData: any;
  constructor(public cartService : CartService, private router : Router) { }

  ngOnInit(): void {  
  } 

  onOrder(){
    this.modalData = this.cartService.getAmount();
    this.showModal = true;    
  }

  onModalClose(): void {
    this.showModal = false;
    this.cartService.clear();
    this.router.navigateByUrl('');  
    console.log("Back to the future !");  
  }
}
