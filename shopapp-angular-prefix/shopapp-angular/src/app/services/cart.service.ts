import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {Product} from "../models/product";
import {Category} from "../models/category";
import {ProductService} from "./product.service";
// import {LocalStorageService} from 'ngx-webstorage'

@Injectable({
  providedIn: 'root'
})
export class CartService{
  private cart: Map<number, number> = new Map();
  constructor(private productService: ProductService) {
    const storedCart = localStorage.getItem('cart');
    if (storedCart){
      this.cart = new Map(JSON.parse(storedCart));
    }
  }
  addToCart(productId: number, quantity: number = 1) {
    debugger;
    if (this.cart.has(productId)) {
      this.cart.set(productId, this.cart.get(productId)! + quantity);
    } else {
      this.cart.set(productId, quantity);
    }
    this.saveCartToLocalStorage();
  }
  getCart():Map<number,number>{
      return this.cart;
  }
  private saveCartToLocalStorage(){
    debugger
    localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())));
  }
  clearCart(){
    this.cart.clear();
    this.saveCartToLocalStorage();
  }
}
