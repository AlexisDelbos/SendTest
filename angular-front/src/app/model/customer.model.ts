export class Customer {
    name : string;
    firstName : string;
    address : string;
    phone : string;
    email : string;

    constructor(name : string,firstName : string, address : string,phone : string, email:string){
        this.name = name;
        this.firstName = firstName;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}