import {Router, RouterModule, Routes} from "@angular/router";
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {DetailProductComponent} from "./components/detail-product/detail-product.component";
import {OrderComponent} from "./components/order/order.component";
import {OrderDetailComponent} from "./components/detail-product/order-detail/order.detail.component";
import {NgModule} from "@angular/core";
import {AuthGuard, AuthGuardFn} from "./guards/auth.guard";
import {UserProfileComponent} from "./components/user-profile/user-profile.component";
import {ChangesPasswordComponent} from "./components/changes-password/changes-password.component";
import {AdminComponent} from "./components/admin/admin.component";
import {AdminGuardFn} from "./guards/admin.guard";
// import {OrderAdminComponent} from "./components/admin/orders/order-admin.component";
// import {CategoryAdminComponent} from "./components/admin/categories/category-admin.component";
// import {ProductAdminComponent} from "./components/admin/products/product-admin.component";
import {CommonModule} from "@angular/common";
import {MyPurchaseComponent} from "./my-purchase/my-purchase.component";
import {UpdateUserComponent} from "./components/user-profile/update-user/update-user.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'products/:id', component: DetailProductComponent },
  { path: 'orders', component: OrderComponent,canActivate:[AuthGuardFn] },
  { path: 'user-profile', component: UserProfileComponent,canActivate:[AuthGuardFn] },
  { path: 'changes-password', component: ChangesPasswordComponent,canActivate:[AuthGuardFn] },
  { path: 'my-purchase', component: MyPurchaseComponent,canActivate:[AuthGuardFn] },
  { path: 'orders/:id', component: OrderDetailComponent },
  { path: 'users/update', component: UpdateUserComponent },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate:[AdminGuardFn]
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    CommonModule
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
