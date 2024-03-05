export interface Coupon{
  id: number;
  code: string;
  discount_value: number;
  discount_type: string;
  expiration_date: Date;
}
