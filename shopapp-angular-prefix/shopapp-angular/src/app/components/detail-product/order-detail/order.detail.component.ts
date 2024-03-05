import {Component, OnInit} from '@angular/core';
import {Product} from "../../../models/product";
import {CartService} from "../../../services/cart.service";
import {ProductService} from "../../../services/product.service";
import {environment} from "../../../environments/environment";
import {CouponService} from "../../../services/coupon.service";
import {Coupon} from "../../../models/coupon";
import {OrderResponse} from "../../../responses/order/order.response";
import {OrderService} from "../../../services/order.service";
import {OrderDetail} from "../../../models/order.detail";
import {ActivatedRoute, Router} from "@angular/router";
import {PaymentService} from "../../../services/payment.service";

@Component({
  selector: 'app-order-detail',
  templateUrl: './order.detail.component.html',
  styleUrls: ['./order.detail.component.scss']
})
export class OrderDetailComponent implements OnInit{
  cartItems: {product: Product, quantity: number} [] = [];
  couponCode: string = "";
  totalAmount: number = 0;
  discountValue: number = 0;
  orderId: number=0;
  total: number = 0;
  coupons: Coupon[] = [];
  orderResponse: OrderResponse = {
    id: 0,
    user_id: 0,
    fullname: '',
    phone_number: '',
    email: '',
    address: '',
    note: '',
    order_date: new Date(),
    status: '',
    total_money: 0,
    shipping_method: '',
    shipping_address: '',
    shipping_date: new Date(),
    payment_method: '',
    order_details: [],
    active: true
  }
  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private couponService: CouponService,
    private orderService: OrderService,
    private router: ActivatedRoute,
    private paymentService: PaymentService
  ) {
  }

  ngOnInit(): void {
      debugger;
      this.getOrderDetails();
    }
    getOrderDetails(){
      debugger;
      this.router.params.subscribe(params => {
        this.orderId = params['id'];
        debugger;
      });
      this.orderService.getOrderById(this.orderId).subscribe({
        next: (response: any) => {
          this.orderResponse.id = response.id;
          this.orderResponse.user_id = response.user_id;
          this.orderResponse.fullname = response.fullname;
          this.orderResponse.email = response.email;
          this.orderResponse.phone_number = response.phone_number;
          this.orderResponse.address = response.address;
          this.orderResponse.note = response.note;
          this.orderResponse.active = response.active;
          this.orderResponse.order_date = new Date(
            response.order_date[0],
            response.order_date[1]-1,
            response.order_date[2]
          );
          debugger;
          this.orderResponse.order_details = response.order_details.map((order_detail: OrderDetail) => {
            order_detail.product.thumbnail = `${environment.apiBaseUrl}/products/images/${order_detail.product.thumbnail}`;
            return order_detail;
          })
          this.orderResponse.payment_method = response.payment_method;
          this.orderResponse.shipping_date = new Date(
            response.shipping_date[0],
            response.shipping_date[1]-1,
            response.shipping_date[2]
          );
          this.orderResponse.shipping_method = response.shipping_method;
          this.orderResponse.status = response.status;
          this.orderResponse.total_money = response.total_money;
          this.totalAmount = this.orderResponse.total_money;
          this.calculateTotal();
        },
        complete: () => {
          debugger;
        },
        error: (error : any) => {
          debugger;
          console.log("error fetching data: ",error.getMessage());
        }
      })
    }
    calculateTotal(){
      this.orderResponse.order_details.forEach(item => {
        this.total += item.quantity*item.product.price;
      })
    }
    applyCoupon(){
    this.discountValue = 0;
       this.couponService.getCoupons().subscribe({
         next: (response: Coupon[]) => {
           debugger;
           response.forEach((coupon : Coupon) => {
             debugger;
             if (coupon.code === this.couponCode){
               this.discountValue = coupon.discount_value;
             }
           })
         },
         complete: () => {
           debugger;
         },
         error: (error: any) => {
           debugger;
           console.log("Error fetching data: "+error.getMessage());
         }
       });
    }
    onCouponChange() {
      console.log(this.couponCode);
    }
    purchaseOrder(){
      debugger;
      this.paymentService.getCreatePayment(this.orderResponse.total_money, this.orderResponse.id).subscribe({
        next: (response: any) =>{
          debugger;
          window.location.href = response.url;
        },
        complete: () =>{
          debugger;

        },
        error: (error: any) =>{
          debugger;
          console.log("Error fetching data: error ",error.error.message);
        }
      })
    }
}
