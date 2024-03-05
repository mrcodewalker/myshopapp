import {Component, OnInit} from '@angular/core';
import {OrderResponse} from "../../../responses/order/order.response";
import {OrderService} from "../../../services/order.service";
import {TokenService} from "../../../services/token.service";
import {Router} from "@angular/router";
import {Product} from "../../../models/product";
import {ProductService} from "../../../services/product.service";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-product-admin',
  templateUrl: './product-admin.component.html',
  styleUrls: ['./product-admin.component.scss']
})
export class ProductAdminComponent implements OnInit{
  products: Product[] =[];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] =[];
  totalPages: number = 0;
  keyword: string = "";
  visiblePages: number[] = [];
  constructor(
    private productService: ProductService,
    private tokenService: TokenService,
    private router: Router
  ) {
  }
  showImage(url: string){
    return `${environment.apiBaseUrl}/products/images/`+url;
  }
  searchProducts() {
    this.currentPage = 0;
    this.itemsPerPage = 12;
    debugger
    this.getAllProducts(this.keyword.trim(), this.currentPage, this.itemsPerPage);
  }
  ngOnInit() {
    this.getAllProducts(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getAllProducts(keyword: string, page: number, limit: number){
    this.productService.getAllProducts(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.products = response.products;
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
    localStorage.setItem('currentProductAdminPage', String(this.currentPage));
    this.getAllProducts(this.keyword, this.currentPage, this.itemsPerPage);
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
  deleteProduct(id:number) {
    const confirmation = window
      .confirm('Are you sure you want to delete this order?');
    if (confirmation) {
      debugger
      this.productService.deleteProduct(id).subscribe({
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
  viewDetails(product: Product) {
    debugger
    this.router.navigate(['/admin/products',product.id]);
  }
}

