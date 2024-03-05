import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PaymentService} from "../services/payment.service";
import {OrderService} from "../services/order.service";
import {OrderResponse} from "../responses/order/order.response";
import {environment} from "../environments/environment";

@Component({
  selector: 'app-my-purchase',
  templateUrl: './my-purchase.component.html',
  styleUrls: ['./my-purchase.component.scss']
})
export class MyPurchaseComponent implements OnInit{
    orders: OrderResponse[] = [];
    totalPages: number = 0;
    currentPage: number = 0;
    visiblePages: number[] = [];
    itemsPerPage: number = 12;
    constructor(
      private router: Router,
      private route: ActivatedRoute,
      private paymentService: PaymentService,
      private orderService: OrderService
    ) {
    }
    ngOnInit() {
      debugger;
      this.getOrderPurchase(this.currentPage, this.itemsPerPage);
    }
    getOrderPurchase(page: number, limit: number){
      this.orderService.getOrdersByUserId(page, limit).subscribe({
        next: (response: any) =>{
          debugger;
          this.orders = response.orders;
          this.totalPages = response.total_pages;
          debugger;
          this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
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
  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    if (currentPage<0||totalPages<0){
      return [];
    }
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    return new Array(endPage - startPage + 1).fill(0)
      .map((_, index) => startPage + index);
  }
  onProductClick(productId: number) {
    debugger;
    this.router.navigate(['/products', productId]);
  }
  onOrderClick(id: number){
      debugger;
      this.router.navigate(['/orders', id]);
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    localStorage.setItem('currentProductPage', String(this.currentPage));
    this.getOrderPurchase(this.currentPage, this.itemsPerPage);
  }
  showImage(url: string){
    return `${environment.apiBaseUrl}/products/images/`+url;
  }

  protected readonly window = window;
}
