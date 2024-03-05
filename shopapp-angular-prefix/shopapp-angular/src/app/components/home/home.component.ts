import {Component, OnInit} from '@angular/core';
import {environment} from "../../environments/environment";
import {Product} from "../../models/product";
import {ProductService} from "../../services/product.service";
import {Category} from "../../models/category";
import {CategoryService} from "../../services/category.service";
import {Router} from "@angular/router";
import {PaymentService} from "../../services/payment.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit{
  products: Product[] = [];
  categories: Category[] = [];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = '';
  selectedCategoryId: number = 0;
  constructor(private productService: ProductService,
              private categoryService: CategoryService,
              private router: Router,
              private paymentService: PaymentService) {
  }
    ngOnInit(): void {
        this.getCategories(this.currentPage, this.itemsPerPage);
        this.getProducts(this.keyword,this.selectedCategoryId,this.currentPage, this.itemsPerPage);
        console.log(new URLSearchParams(window.location.search));
        if (window.location.href!=`http://localhost:4300/`){
          const queryString = window.location.search;
          const urlParams = new URLSearchParams(queryString);
          const vnp_Amount = urlParams.get('vnp_Amount');
          const vnp_BankCode = urlParams.get('vnp_BankCode');
          const vnp_ResponseCode = urlParams.get('vnp_ResponseCode');
          const vnp_OrderInfo = urlParams.get('vnp_OrderInfo');
          if (vnp_Amount !== null && vnp_BankCode !== null && vnp_ResponseCode !== null && vnp_OrderInfo !== null) {
            this.paymentService.getInfoPayment(Number(vnp_Amount), vnp_BankCode, vnp_ResponseCode, vnp_OrderInfo,(Number) (vnp_OrderInfo)).subscribe({
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
    }
    getCategories(page: number, limit: number){
      this.categoryService.getCategories(page,limit).subscribe({
        next: (categories: Category[]) => {
          debugger;
          this.categories = categories;
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          console.log('Error fetching categories', error);
        }
        }
      )
    }
    getProducts(keyword: string, selectedCategoryId:number, page: number, limit: number){
      this.productService.getProducts(keyword,selectedCategoryId, page, limit).subscribe({
        next: (response: any) => {
          debugger;
          response.products.forEach((product: Product) => {
            debugger
            product.url = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`
          });
          debugger;
            this.products = response.products;
            this.totalPages = response.total_pages;
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
    searchProducts() {
    this.currentPage = 0;
    this.itemsPerPage = 12;
    debugger;
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
    }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    localStorage.setItem('currentProductPage', String(this.currentPage));
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
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
}
