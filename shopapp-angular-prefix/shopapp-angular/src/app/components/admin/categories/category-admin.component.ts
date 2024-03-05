import {Component, OnInit} from '@angular/core';
import {OrderResponse} from "../../../responses/order/order.response";
import {OrderService} from "../../../services/order.service";
import {TokenService} from "../../../services/token.service";
import {Router} from "@angular/router";
import {Category} from "../../../models/category";
import {CategoryService} from "../../../services/category.service";

@Component({
  selector: 'app-category-admin',
  templateUrl: './category-admin.component.html',
  styleUrls: ['./category-admin.component.scss']
})
export class CategoryAdminComponent implements OnInit{
  categories: Category[] =[];
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] =[];
  totalPages: number = 0;
  keyword: string = "";
  visiblePages: number[] = [];
  constructor(
    private categoryService: CategoryService,
    private tokenService: TokenService,
    private router: Router
  ) {
  }
  searchCategories() {
    this.itemsPerPage = 12;
    debugger
    this.getAllCategories(this.keyword.trim(), this.currentPage, this.itemsPerPage);
  }
  ngOnInit() {
    this.getAllCategories(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getAllCategories(keyword: string, page: number, limit: number){
    this.categoryService.getAllCategories(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.categories = response.categories;
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
    localStorage.setItem('currentCategoriesAdminPage', String(this.currentPage));
    this.getAllCategories(this.keyword, this.currentPage, this.itemsPerPage);
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
  deleteCategory(id:number) {
    const confirmation = window
      .confirm('Are you sure you want to delete this order?');
    if (confirmation) {
      debugger
      this.categoryService.deleteCategory(id).subscribe({
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
  viewDetails(category: Category) {
    debugger
    this.router.navigate(['/admin/categories', category.id]);
  }
}

