import {Product} from "../../models/product";
import {OrderDetail} from "../../models/order.detail";

export interface OrderResponse {
  id: number,
  user_id: number,
  fullname: string,
  phone_number: string,
  email: string,
  address: string,
  note: string,
  order_date: Date,
  status: string,
  total_money: number,
  shipping_method: string,
  shipping_address: string,
  shipping_date: Date,
  payment_method: string,
  order_details: OrderDetail [],
  active: boolean
}
