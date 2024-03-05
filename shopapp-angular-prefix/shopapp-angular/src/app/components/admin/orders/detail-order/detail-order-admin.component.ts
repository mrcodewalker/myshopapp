import {Component, OnInit} from '@angular/core';
import {Product} from "../../../../models/product";
import {Coupon} from "../../../../models/coupon";
import {OrderResponse} from "../../../../responses/order/order.response";
import {CartService} from "../../../../services/cart.service";
import {ProductService} from "../../../../services/product.service";
import {CouponService} from "../../../../services/coupon.service";
import {OrderService} from "../../../../services/order.service";
import {ActivatedRoute, Router, Routes} from "@angular/router";
import {OrderDetail} from "../../../../models/order.detail";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-detail-order-admin',
  templateUrl: './detail-order-admin.component.html',
  styleUrls: ['./detail-order-admin.component.scss']
})
export class DetailOrderAdminComponent implements OnInit{
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
    private route: Router
  ) {
  }

  ngOnInit(): void {
    debugger;
    this.getOrderDetails();
  }
  getOrderDetails(){
    debugger;
    this.orderId = Number(this.router.snapshot.paramMap.get('id'));
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
        debugger;
        const day = this.orderResponse.order_date.getDay();
        const month = this.orderResponse.order_date.getMonth()+1;
        const year = this.orderResponse.order_date.getFullYear();
        const date = new Date(this.orderResponse.order_date);
        this.orderResponse.order_date = date;
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
        this.orderResponse.shipping_address = response.shipping_address;
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
  updateOrder(orderResponse: OrderResponse){
    if (!orderResponse.active){
      alert("You can not update this order because this order removed");
      this.route.navigate(['../'],{relativeTo: this.router});
      return;
    }
    debugger;
    this.orderService.updateOrder(orderResponse.id, this.orderResponse)
      .subscribe({
        next: (response: any) => {
          debugger;
          console.log('Order updated successfully: ',response);
          this.route.navigate(['../'], {relativeTo: this.router});
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          console.log("Error fetching data: "+error.error.message);
        }
      })
  }
}

