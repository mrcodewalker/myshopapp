import {Component, OnInit} from '@angular/core';
import {OrderDto} from "../../../dtos/order/order.dto";
import {OrderService} from "../../../services/order.service";
import {TokenService} from "../../../services/token.service";
import {Product} from "../../../models/product";
import {environment} from "../../../environments/environment";
import {OrderResponse} from "../../../responses/order/order.response";
import {Router} from "@angular/router";

@Component({
  selector: 'app-order-admin',
  templateUrl: './order-admin.component.html',
  styleUrls: ['./order-admin.component.scss']
})
export class OrderAdminComponent implements OnInit{
    orders: OrderResponse[] =[];
    currentPage: number = 0;
    itemsPerPage: number = 12;
    pages: number[] =[];
    totalPages: number = 0;
    keyword: string = "";
    visiblePages: number[] = [];
    constructor(
      private orderService: OrderService,
      private tokenService: TokenService,
      private router: Router
    ) {
    }
  searchOrders() {
    this.currentPage = 0;
    this.itemsPerPage = 12;
    debugger
    this.getAllOrders(this.keyword.trim(), this.currentPage, this.itemsPerPage);
  }
    ngOnInit() {
      this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
    }
  getAllOrders(keyword: string, page: number, limit: number){
    this.orderService.getAllOrders(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.orders = response.orders;
        this.totalPages = response.total_pages;
        debugger;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error("error fetching products: ", error);
      }
    })
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    localStorage.setItem('currentOrderAdminPage', String(this.currentPage));
    this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
  }

  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
      debugger;
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
  deleteOrder(id:number) {
    const confirmation = window
      .confirm('Are you sure you want to delete this order?');
    if (confirmation) {
      debugger
      this.orderService.deleteOrder(id).subscribe({
        next: (response: any) => {
          debugger
          location.reload();
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger;
          console.error('Error fetching products:', error);
        }
      });
    }
  }
  viewDetails(order:OrderResponse) {
    debugger
    this.router.navigate(['/admin/orders', order.id]);
  }
}
