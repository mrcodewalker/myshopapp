import {Component, OnInit} from '@angular/core';
import {Category} from "../../../models/category";
import {Coupon} from "../../../models/coupon";
import {CouponService} from "../../../services/coupon.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-coupon-admin',
  templateUrl: './coupon-admin.component.html',
  styleUrls: ['./coupon-admin.component.scss']
})
export class CouponAdminComponent implements OnInit{
    coupons: Coupon[] =[];
    currentPage: number = 0;
    itemsPerPage: number = 12;
    pages: number[] =[];
    today: Date = new Date();
    totalPages: number = 0;
    keyword: string = "";
    visiblePages: number[] = [];
    constructor(
      private couponService: CouponService,
      private router: Router,
      private route: ActivatedRoute
    ) {
    }
    ngOnInit() {
      debugger;
      this.getAllCoupons(this.keyword, this.currentPage, this.itemsPerPage);
    }
    searchCoupons(){
      this.getAllCoupons(this.keyword.trim(), this.currentPage, this.itemsPerPage);
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
  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    localStorage.setItem('currentCategoriesAdminPage', String(this.currentPage));
    this.getAllCoupons(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getAllCoupons(keyword: string, page: number, limit: number){
      debugger;
    this.couponService.getAllCoupons(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.coupons = response.coupons;
        this.totalPages = response.total_pages;
        debugger;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error("error fetching coupons: ", error);
      }
    })
  }
}
