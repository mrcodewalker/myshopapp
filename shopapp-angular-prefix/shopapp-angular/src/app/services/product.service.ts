import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {Product} from "../models/product";
import {Coupon} from "../models/coupon";
@Injectable({
  providedIn: 'root'
})

export class ProductService {
  private apiGetProducts = `${environment.apiBaseUrl}/products`;
  private apiGetAllProducts = `${environment.apiBaseUrl}/products/get-products-by-keyword`;
  constructor(private http: HttpClient) {
  }
  getProducts(keyword: string, selectedCategoryId: number, page: number, limit: number): Observable<Product[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString())
      .set('keyword', keyword.toString())
      .set('category_id', selectedCategoryId.toString())
    return this.http.get<Product[]>(this.apiGetProducts, {params});
  }
  getDetailProduct(productId: number){
    return this.http.get<Product[]>(this.apiGetProducts+"/"+productId)
  }
  getProductsByIds(productIds: number[]): Observable<Product[]>{
      debugger;
      const params = new HttpParams().set('ids', productIds.join(','));
      return this.http.get<Product[]>(this.apiGetProducts+"/by-ids", {params});
  }
  getAllProducts(keyword: string, page: number, limit: number): Observable<any>{
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString())
      .set('keyword', keyword.toString());
    return this.http.get(this.apiGetAllProducts, {params});
  }
  deleteProduct(id: number): Observable<any>{
    return this.http.delete(this.apiGetProducts+"/"+id.toString());
  }
  updateProduct(product: Product): Observable<any>{
    return this.http.put(this.apiGetProducts+"/"+product.id.toString(), product);
  }
}
