import { Injectable } from '@angular/core';
import { Customer } from '../model/customer.model';
import { Training } from '../model/training.model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cart : Map<number,Training>;
  private cartItems = new BehaviorSubject<number>(0);   // création d'un osbervable
  cartItems$ = this.cartItems.asObservable();           // Observable exposé

  constructor() {     
    let cart = localStorage.getItem('cart');
    if(cart){ 
      this.cart = new Map(JSON.parse(cart));
    }
    else this.cart = new Map<number,Training>();

    this.cartItems.next(this.cart.size);          // Observable alimenté
  }

  addTraining(training: Training) { 
    this.cart.set(training.id,training);
    this.saveCart(); 
    this.cartItems.next(this.cart.size);          // Mise à jour de l'observable
  }

  saveCustomer(customer : Customer) {
    localStorage.setItem('customer',JSON.stringify(customer));
  }

  saveCart() {
    localStorage.setItem('cart',JSON.stringify([...this.cart]));
  }

  removeTraining(training: Training) {
    this.cart.delete(training.id);
    this.saveCart();
    this.cartItems.next(this.cart.size);          // Mise à jour de l'observable
  }

  getCart() {
    return Array.from(this.cart.values());
  }

  getSize() {    
    return this.cart.size;
  }

  getAmount() : number {
    let amount : number = 0;
    this.cart.forEach(training => {
      amount += training.price * training.quantity;
    });
    return amount;    
  }

  getCustomer() : Customer {
    let customer = localStorage.getItem('customer');
    if(customer)  return  JSON.parse(customer);
    return new Customer("unknown","","","","");
  }

  clear() {
    this.cart.clear();
    localStorage.removeItem('cart');
    this.cartItems.next(0);
  }
}
