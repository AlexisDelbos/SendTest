import { Customer } from "./customer.model";
import { Training } from "./training.model";

export class Cart {
    customer : Customer;
    items : Map<number,Training>;

    constructor(){
        this.customer = new Customer("unknown","","","","");
        this.items = new Map<number,Training>();
    }
}