import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../environments/environment";
import {OrderDto} from "../dtos/order/order.dto";
import {Observable} from "rxjs";
import {OrderResponse} from "../responses/order/order.response";
@Injectable({
  providedIn: 'root'
})

export class PaymentService{
  private apiCreatePayment = `${environment.apiBaseUrl}/payments/create_payment`;
  private apiGetInfoPayment = `${environment.apiBaseUrl}/payments/check_payment`;
  constructor(private http: HttpClient) {
  }
  getCreatePayment(total_money: number, order_id: number): Observable<any>{
    const params = new HttpParams()
      .set('total_money', total_money)
      .set('order_id', order_id);
    return this.http.get(this.apiCreatePayment, {params});
  }
  getInfoPayment(
    vnp_Amount: number,
    vnp_BankCode: string,
    vnp_ResponseCode: string,
    vnp_OrderInfo: string,
    order_id: number
  ): Observable<any>{
    const params = new HttpParams()
      .set('vnp_Amount', vnp_Amount.toString())
      .set('vnp_BankCode', vnp_BankCode.toString())
      .set('vnp_ResponseCode', vnp_ResponseCode.toString())
      .set('vnp_OrderInfo', vnp_OrderInfo.toString())
      .set('order_id', order_id);
    return this.http.get(this.apiGetInfoPayment, {params} );
  }
}
