import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../environments/environment";
import {OrderDto} from "../dtos/order/order.dto";
import {Observable} from "rxjs";
import {OrderResponse} from "../responses/order/order.response";
import {UserService} from "./user.service";
@Injectable({
  providedIn: 'root'
})

export class OrderService{
  private apiOrderBase = `${environment.apiBaseUrl}/orders`;
  private apiGetAllOrders = `${environment.apiBaseUrl}/orders/get-orders-by-keyword`;
  private apiGetOrdersByUserId = `${environment.apiBaseUrl}/orders/get_orders_by_user_id`;
  private apiGetOrdersByProductId = this.apiOrderBase+"/get_delivered_orders/";
  constructor(private http: HttpClient,
              private userService: UserService) {
  }
  placeOrder(orderData: OrderDto): Observable<any>{
    return this.http.post(this.apiOrderBase, orderData);
  }
  getOrderById(id: number): Observable<any>{
      return this.http.get<OrderResponse>(this.apiOrderBase+`/${id}`);
  }
  getAllOrders(keyword: string,
               page:number,
               limit: number): Observable<any>{
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('limit', limit.toString())
      .set('page', page.toString());
    return this.http.get<any>(this.apiGetAllOrders, {params});
  }
  deleteOrder(id: number): Observable<any>{
    return this.http.delete(this.apiOrderBase+"/"+id.toString());
  }
  updateOrder(orderId: number, orderResponse: OrderResponse){
    return this.http.put(this.apiOrderBase+"/"+orderId.toString(), orderResponse);
  }
  getOrdersByUserId(
    page: number,
    limit: number
  ):Observable<any>{
    const params = new HttpParams()
      .set('limit', limit.toString())
      .set('page', page.toString());
    const userId = this.userService.getUserResponseFromLocalStorage()?.id;
    return this.http.get<any>(this.apiOrderBase+`/list/`+userId?.toString(), {params});
  }
  getAllOrdersByUserId(): Observable<any>{
    return this.http.get(this.apiGetOrdersByUserId+`/`+this.userService.getUserResponseFromLocalStorage()?.id.toString());
  }
  getOrdersByProductId(product_id: number) : Observable<any>{
    return this.http.get(this.apiGetOrdersByProductId+product_id.toString());
  }
}
