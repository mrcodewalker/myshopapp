import {RouterModule, Routes} from "@angular/router";
import {AdminComponent} from "./admin.component";
import {OrderAdminComponent} from "./orders/order-admin.component";
import {ProductAdminComponent} from "./products/product-admin.component";
import {CategoryAdminComponent} from "./categories/category-admin.component";
import {NgModule} from "@angular/core";
import {DetailOrderAdminComponent} from "./orders/detail-order/detail-order-admin.component";
import {DetailProductAdminComponent} from "./products/detail-product/detail-product-admin.component";
import {DetailCategoryAdminComponent} from "./categories/detail-category/detail-category-admin.component";
import {CouponAdminComponent} from "./coupons/coupon-admin.component";
import {UsersAdminComponent} from "./users/users-admin.component";

const routes: Routes = [
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      {
        path: 'orders',
        component: OrderAdminComponent
      },
      {
        path: 'orders/:id',
        component: DetailOrderAdminComponent
      },
      {
        path: 'products',
        component: ProductAdminComponent
      },
      {
        path: 'products/:id',
        component: DetailProductAdminComponent
      },
      {
        path: 'categories/:id',
        component: DetailCategoryAdminComponent
      },
      {
        path: 'categories',
        component: CategoryAdminComponent
      },
      {
        path: 'coupons',
        component: CouponAdminComponent
      },
      {
        path: 'users',
        component: UsersAdminComponent
      },
    ]
  }
];
@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
