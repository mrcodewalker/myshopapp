import {Product} from "./product";

export interface OrderDetail {
  id: number,
  product: Product,
  price: number,
  color: string,
  quantity: number,
  total_money: number
}
