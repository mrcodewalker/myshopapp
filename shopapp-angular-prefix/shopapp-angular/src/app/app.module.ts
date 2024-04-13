  import { NgModule } from '@angular/core';
  import { BrowserModule } from '@angular/platform-browser';
  import { HomeComponent } from './components/home/home.component';
  import { HeaderComponent } from './components/header/header.component';
  import { FooterComponent } from './components/footer/footer.component';
  import { DetailProductComponent } from './components/detail-product/detail-product.component';
  import { OrderComponent } from './components/order/order.component';
  import {
    OrderDetailComponent
  } from './components/detail-product/order-detail/order.detail.component';
  import { LoginComponent } from './components/login/login.component';
  import { RegisterComponent } from './components/register/register.component';
  import {FormsModule, ReactiveFormsModule} from "@angular/forms";
  import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
  import {TokenInterceptor} from "./interceptors/token.interceptor";
  import {NgOptimizedImage} from "@angular/common";
  import { AppComponent } from './app/app.component';
  import {AppRoutingModule} from "./app-routing.module";
  import {NgbPopover, NgbPopoverModule} from "@ng-bootstrap/ng-bootstrap";
  import { UserProfileComponent } from './components/user-profile/user-profile.component';
  import { ChangesPasswordComponent } from './components/changes-password/changes-password.component';
  import {AdminModule} from "./components/admin/admin.module";
import { DetailOrderAdminComponent } from './components/admin/orders/detail-order/detail-order-admin.component';
import { DetailProductAdminComponent } from './components/admin/products/detail-product/detail-product-admin.component';
import { DetailCategoryAdminComponent } from './components/admin/categories/detail-category/detail-category-admin.component';
  import {NzIconModule} from "ng-zorro-antd/icon";
  import {NzButtonModule} from "ng-zorro-antd/button";
  import {NzListModule} from "ng-zorro-antd/list";
  import {NzPageHeaderModule} from "ng-zorro-antd/page-header";
  import {NzCardModule} from "ng-zorro-antd/card";
  import {NzDemoButtonBasicComponent} from "./antd-components/NzDemoButtonBasicComponent";
import { MyPurchaseComponent } from './my-purchase/my-purchase.component';
import { CouponAdminComponent } from './components/admin/coupons/coupon-admin.component';
import { UsersAdminComponent } from './components/admin/users/users-admin.component';
import { UpdateUserComponent } from './components/user-profile/update-user/update-user.component';
  // import { AdminComponent } from './components/admin/admin.component';
  // import { OrderAdminComponent } from './components/admin/orders/order-admin.component';
  // import { CategoryAdminComponent } from './components/admin/categories/category-admin.component';
  // import { ProductAdminComponent } from './components/admin/products/product-admin.component';
  @NgModule({
    declarations: [
      HomeComponent,
      HeaderComponent,
      FooterComponent,
      DetailProductComponent,
      OrderComponent,
      OrderDetailComponent,
      LoginComponent,
      RegisterComponent,
      AppComponent,
      UserProfileComponent,
      ChangesPasswordComponent,
      DetailOrderAdminComponent,
      DetailProductAdminComponent,
      DetailCategoryAdminComponent,
      NzDemoButtonBasicComponent,
      MyPurchaseComponent,
      CouponAdminComponent,
      UsersAdminComponent,
      UpdateUserComponent,
      // AdminComponent,
      // OrderAdminComponent,
      // CategoryAdminComponent,
      // ProductAdminComponent,
      // CategoryAdminComponent
    ],
    imports: [
      ReactiveFormsModule,
      BrowserModule,
      FormsModule,
      HttpClientModule,
      NgOptimizedImage,
      AppRoutingModule,
      NgbPopover,
      NgbPopoverModule,
      AdminModule,
      NzIconModule,
      NzButtonModule,
      NzListModule,
      NzPageHeaderModule,
      NzCardModule,
    ],
    providers: [
      {
        provide: HTTP_INTERCEPTORS,
        useClass: TokenInterceptor,
        multi: true
      }
    ],
    bootstrap: [
      // HomeComponent,
      // DetailProductComponent,
      // OrderComponent,
      // OrderDetailComponent,
      // LoginComponent,
      // RegisterComponent
      AppComponent
    ]
  })
  export class AppModule { }
