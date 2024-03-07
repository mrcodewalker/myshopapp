import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/product";
import {Coupon} from "../../models/coupon";
import {CartService} from "../../services/cart.service";
import {ProductService} from "../../services/product.service";
import {CouponService} from "../../services/coupon.service";
import {OrderDto} from "../../dtos/order/order.dto";
import {environment} from "../../environments/environment";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {OrderService} from "../../services/order.service";
import {Router} from "@angular/router";
import {Provinces} from "../../models/provinces";
import {Province} from "../../models/province";
import {District} from "../../models/district";
import {Commune} from "../../models/commune";
import {ProvinceService} from "../../services/province.service";
import {PaymentService} from "../../services/payment.service";
import {CreatePaymentDto} from "../../dtos/payment/create.payment.dto";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit{
  orderForm: FormGroup;
  cartItems: {product: Product, quantity: number} [] = [];
  couponCode: string = "";
  orderId: number = 0;
  totalAmount: number = 0;
  homeUrl = window.location.href;
  discountValue: number = 0;
  expirationCouponDate?: Date;
  createPayment?: CreatePaymentDto;
  orderData: OrderDto = {
    user_id: 1,
    fullname: '',
    email: '',
    address: '',
    phone_number: '',
    shipping_address: '',
    note: '',
    total_money: 0,
    payment_method: 'cod',
    shipping_method: 'express',
    coupon_code: '',
    cart_items: []
  };
  provinceData: Provinces[] = [];
  provinces: Province[] = [];
  districts: District[] = [];
  communes: Commune[] = [];
  communeId: number = 0;
  selectedProvince?: Province;
  selectedDistrict?: District;
  selectedCommune?: Commune;
  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private couponService: CouponService,
    private orderService: OrderService,
    private fb: FormBuilder,
    private router: Router,
    private provinceService: ProvinceService,
    private paymentService: PaymentService,
    private userService: UserService
  ) {
    this.orderForm = this.fb.group({
      fullname: ['', Validators.required],
      email: ['', [Validators.email]],
      phone_number: ['', [Validators.required, Validators.minLength(6)]],
      shipping_address: ['', [Validators.required, Validators.minLength(5)]],
      note: [''],
      address: [''],
      shipping_method: ['express'],
      payment_method: ['cod']
    });
  }
  ngOnInit(): void {
    debugger;
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys());
    debugger;
    this.provinceService.getAllProvinces().subscribe({
      next: (response: any[]) => {
        debugger;
        this.provinces = response;
        debugger;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        console.log("Error fetching data: ",error.error.message);
      }
    });
    if (productIds.length === 0) {
      return ;
    }
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {
        debugger;
        this.cartItems = productIds.map((productId) => {
          debugger;
          const product = products.find((p) => p.id === productId);
          if (product) {
            product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }
          return {
            product: product!,
            quantity: cart.get(productId)!
          };
        });
        console.log('haha');
      },
      complete: () => {
        debugger;
        this.calculateTotal();
      },
      error: (error: any) => {
        debugger;
        console.log("error fetching detail", error);
      }
    });
  }
  calculateTotal(){
    this.cartItems.forEach(item => {
      this.totalAmount += item.quantity*item.product.price;
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
            this.expirationCouponDate = new Date(coupon.expiration_date);
            if (this.expirationCouponDate < new Date()) {
              this.discountValue = coupon.discount_value;
            }
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
  placeOrder(){
    debugger;
    if (this.communes.length===0||this.districts.length===0||this.provinces.length===0){
      alert("Please select your city");
    }
    if (this.orderForm.valid){
      // spread operator
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value
      };
      this.orderData.user_id = Number(this.userService.getUserResponseFromLocalStorage()?.id);
      if (this.selectedCommune==undefined||this.selectedProvince==undefined||this.selectedDistrict==undefined){
        alert("Please check your select province");
      }
      this.orderData.shipping_address = this.orderForm.get('shipping_address')?.value +", "+ this.selectedCommune?.commune_name + `, ${this.selectedDistrict?.district_name}, ${this.selectedProvince?.province_name}`;
      debugger;
      this.orderData.cart_items = this.cartItems.map(cartItem => ({
        product_id: cartItem.product.id,
        quantity: cartItem.quantity
      }));
      const discountedAmount = this.totalAmount * (this.discountValue / 100);
      this.orderData.total_money = this.totalAmount - discountedAmount;
      this.orderData.coupon_code = this.couponCode;
      if (this.orderData.cart_items.length===0){
        alert("Can not order this time, please choose minimum 1 product in your cart");
        return;
      }
      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response: any) => {
          debugger;
          this.orderId = response.order.id;
          if (response.payment_method=='atm'){
            alert("You must purchase for this order");
          }
          if (response.order.payment_method == "cod"){
            this.router.navigate(['/orders/',response.order.id]);
            this.cartService.clearCart();
          } else {
            this.paymentService.getCreatePayment(this.orderData.total_money, this.orderId).subscribe({
              next: (response: any) => {
                debugger;
                this.createPayment = response;
                debugger;
                if (this.createPayment?.status=='OK'){
                  this.cartService.clearCart();
                  window.location.href = this.createPayment.url;
                }
              },
              complete: () =>{
                debugger;
                console.log(this.homeUrl);
                if (this.createPayment?.status=="OK"){
                  debugger;
                  const urlParams = new URLSearchParams(window.location.search);
                  const vnp_Amount = urlParams.get('vnp_Amount');
                  const vnp_BankCode = urlParams.get('vnp_BankCode');
                  const vnp_ResponseCode = urlParams.get('vnp_ResponseCode');
                  const vnp_OrderInfo = urlParams.get('vnp_OrderInfo');
                  if (vnp_Amount !== null && vnp_BankCode !== null && vnp_ResponseCode !== null && vnp_OrderInfo !== null) {
                    this.paymentService.getInfoPayment(Number(vnp_Amount), vnp_BankCode, vnp_ResponseCode, vnp_OrderInfo, this.orderId).subscribe({
                      next: (response: any) =>{
                        debugger;
                        alert(response.message);
                        this.router.navigate(['']);
                      },
                      complete: () => {
                        debugger;
                      },
                      error: (error: any) =>{
                        debugger;
                        console.log("Error fetching data payment method: ",error.error.message);
                      }
                    });
                  } else {
                    console.error('Some values are null.');
                  }
                }
              },
              error: (error: any) =>{
                debugger;
                console.log("Error fetching data payment method: ",error.error.message);
              }
            })
          }
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger;
          alert(`error has been placed when you are ordering: ${error}`)
        }
      });
    } else {
      alert("Information is not correct, please enter form again.")
    }
  }
  updateProvinceValue(event: any){
    const provinceId = event.target.value;
    debugger;
    this.districts.splice(0, this.districts.length);
    this.communes.splice(0, this.communes.length);
    this.communeId=0;
    this.provinceService.getAllDistricts(provinceId).subscribe({
      next: (response: any) => {
        debugger;
        this.districts = response;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log("Error fetching data: "+error.error.message);
      }
    });
    this.provinces.forEach((response) => {
      if (response.province_id==provinceId){
        this.selectedProvince = {
          province_id: response.province_id,
          province_name: response.province_name
        }
        return;
      }
    });
  }
  updateDistrictValue(event: any){
    const districtId = event.target.value;
    debugger;
    this.provinceService.getAllCommunes(districtId).subscribe({
      next: (response: any) => {
        debugger;
        this.communes = response;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log("Error fetching data: "+error.error.message);
      }
    });
    this.districts.forEach((response) => {
      if (response.district_id==districtId){
        this.selectedDistrict = {
          district_id: response.district_id,
          district_name: response.district_name
        }
        return;
      }
    });
    debugger;
  }
  updateCommuneValue(event: any){
    this.communeId = event.target.value;
    debugger;
    this.communes.forEach((response) => {
      if (response.commune_id==this.communeId){
        this.selectedCommune = {
          commune_name: response.commune_name,
          commune_id: response.commune_id
        }
        return;
      }
    });
  }
}
