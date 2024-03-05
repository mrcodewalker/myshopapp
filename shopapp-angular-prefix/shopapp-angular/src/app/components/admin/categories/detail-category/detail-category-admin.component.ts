import {Component, OnInit} from '@angular/core';
import {TokenService} from "../../../../services/token.service";
import {UserService} from "../../../../services/user.service";
import {CategoryService} from "../../../../services/category.service";
import {Category} from "../../../../models/category";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-detail-category-admin',
  templateUrl: './detail-category-admin.component.html',
  styleUrls: ['./detail-category-admin.component.scss']
})
export class DetailCategoryAdminComponent implements OnInit{
  editButton = false;
  category: Category = {
    id: 0,
    name: ''
  };
  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private categoryService: CategoryService,
    private router: ActivatedRoute,
    private route: Router
  ) {
  }
  ngOnInit() {
    this.getCategoryById();
  }
  getCategoryById(){
    const categoryId = Number(this.router.snapshot.paramMap.get('id'));
    debugger;
    this.categoryService.getCategoryById(categoryId).subscribe({
      next: (response: any) => {
        debugger;
        this.category.id = response.id;
        this.category.name = response.name;
        debugger;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log("Error fetching data, error: ",error.error.message);
      }
    });
  }
  updateCategory(){
    this.categoryService.updateCategory(this.category).subscribe({
      next: (response: any) => {
        debugger;
        alert(response.message);
        this.route.navigate(['../'], {relativeTo: this.router});
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log("Error fetching data, error: ",error.error.message);
      }
    })
  }
  clickEditButton(){
    this.editButton = true;
  }
}
