import { NgModule } from '@angular/core';

import { AdminRoutingModule } from './admin-routing.module';

import { AdminComponent } from './admin.component';
import { CommonModule } from '@angular/common';
import {OrderAdminComponent} from "./orders/order-admin.component";
import {CategoryAdminComponent} from "./categories/category-admin.component";
import {ProductAdminComponent} from "./products/product-admin.component";
import {DetailOrderAdminComponent} from "./orders/detail-order/detail-order-admin.component";
import {FormsModule} from "@angular/forms";
import {NzIconModule} from "ng-zorro-antd/icon";
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzDemoButtonBasicComponent} from "../../antd-components/NzDemoButtonBasicComponent";
import {CouponAdminComponent} from "./coupons/coupon-admin.component";
@NgModule({
  declarations: [
    AdminComponent,
    OrderAdminComponent,
    ProductAdminComponent,
    CategoryAdminComponent,
    // CouponAdminComponent,
  ],
  imports: [
    AdminRoutingModule,
    CommonModule,
    FormsModule,
    NzIconModule,
    NzButtonModule,
  ]
})
export class AdminModule {}
