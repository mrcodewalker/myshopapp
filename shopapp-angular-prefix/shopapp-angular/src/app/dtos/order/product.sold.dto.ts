export class ProductSoldDto{
  message: string;
  quantity: number;
  constructor(data: any) {
    this.message = data.message;
    this.quantity = data.quantity;
  }
}
