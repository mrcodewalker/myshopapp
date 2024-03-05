import {
  IsString,
  IsPhoneNumber, IsNotEmpty, IsNumber, ArrayMinSize, ValidateNested
} from 'class-validator';
import {Product} from "../../models/product";
import {CartItemDto} from "./cart.item.dto";
import {Type} from "class-transformer";

export class OrderDto {
  user_id: number;
  fullname: string;
  email: string;
  phone_number: string;
  address: string;
  note: string;
  total_money: number;
  payment_method: string;
  shipping_method: string;
  coupon_code: string;
  shipping_address: string;
  cart_items: {product_id: number, quantity: number}[];
  constructor(data: any) {
    this.shipping_address = data.shipping_address;
    this.address = data.address;
    this.user_id = data.user_id;
    this.fullname = data.fullname;
    this.shipping_method = data.shipping_method;
    this.coupon_code = data.coupon_code;
    this.payment_method = data.payment_method;
    this.phone_number = data.phone_number;
    this.email = data.email;
    this.total_money = data.total_money;
    this.note = data.note;
    this.cart_items = data.cart_items;
  }
}
