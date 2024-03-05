import {
  IsString,
  IsPhoneNumber, IsNotEmpty, IsNumber, ArrayMinSize, ValidateNested
} from 'class-validator';
import {Product} from "../../models/product";

export class CartItemDto{
  @IsNumber()
  product_id: number;

  @IsNumber()
  quantity: number;

  constructor(data: any) {
    this.product_id = data.product_id;
    this.quantity = data.quantity;
  }
}
