import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../environments/environment";
import {Coupon} from "../models/coupon";
import {Observable} from "rxjs";
@Injectable({
  providedIn: 'root'
})

export class CouponService{
  private apiGetCoupons = `${environment.apiBaseUrl}/coupons`;
  constructor(private http: HttpClient) {
  }
  getCoupons(){
    return this.http.get<Coupon[]>(this.apiGetCoupons+"/list");
  }
  getAllCoupons(keyword: string, page:number, limit: number): Observable<any>{
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString())
    return this.http.get(this.apiGetCoupons+"/admin/list", {params});
  }
}
