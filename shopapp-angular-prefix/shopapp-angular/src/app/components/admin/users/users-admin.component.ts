import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../services/user.service";
import {Coupon} from "../../../models/coupon";
import {UserResponse} from "../../../responses/user/user.response";
import {UpdateUserByAdminDto} from "../../../dtos/user/update.user.by.admin.dto";
import {Router} from "@angular/router";

@Component({
  selector: 'app-users-admin',
  templateUrl: './users-admin.component.html',
  styleUrls: ['./users-admin.component.scss']
})
export class UsersAdminComponent implements OnInit{
  users: UserResponse[] =[];
  updateUser: UpdateUserByAdminDto = {
    role_name: "",
    is_active: false
  }
  currentPage: number = 0;
  itemsPerPage: number = 12;
  pages: number[] =[];
  today: Date = new Date();
  totalPages: number = 0;
  keyword: string = "";
  visiblePages: number[] = [];
  constructor(
    private userService: UserService,
    private router: Router
  ) {
  }
  searchUsers(){
    this.getAllUsers(this.keyword.trim(), this.currentPage, this.itemsPerPage);
  }
  ngOnInit() {
    this.getAllUsers(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getAllUsers(keyword: string, page: number, limit: number){
    debugger;
    this.userService.getAllUsers(keyword, page, limit).subscribe({
      next: (response :any) =>{
        debugger;
        this.users = response.users;
        this.totalPages = response.total_pages;
        debugger;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) =>{
        debugger;
        console.log("Error fetching data: error ", error.error.message);
      }
    })

  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    localStorage.setItem('currentCategoriesAdminPage', String(this.currentPage));
    this.getAllUsers(this.keyword, this.currentPage, this.itemsPerPage);
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
  updateUserByAdmin(id: number){
    debugger;
    this.userService.updateUserByAdmin(id, this.updateUser).subscribe({
      next: (response : any) => {
        debugger;
        alert("Update user successfully");
        this.router.navigate(['/users']);
      },
      complete: () => {
        debugger;

      },
      error: (error: any) => {
        debugger;
        console.log("Error fetching data: error ", error.error.message);

      }
    })
  }
  onIsActiveChange(user: UserResponse){
    this.updateUser.is_active = user.is_active;
    this.updateUser.role_name = user.role.name;
  }
  onRoleChange(user: UserResponse){
    this.updateUser.is_active = user.is_active;
    this.updateUser.role_name = user.role.name;
  }
  deleteById(user: UserResponse){
    this.updateUser.is_active = false;
    this.updateUser.role_name = user.role.name;
    this.updateUserByAdmin(user.id);
  }
}
