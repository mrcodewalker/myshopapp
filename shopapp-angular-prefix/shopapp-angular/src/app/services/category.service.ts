import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {Product} from "../models/product";
import {Category} from "../models/category";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiGetCategories = `${environment.apiBaseUrl}/categories`;
  private apiGetAllCategories = `${environment.apiBaseUrl}/categories/get-categories-by-keyword`;

  constructor(private http: HttpClient) {
  }
  getCategories(page: number, limit: number): Observable<Category[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString())
    return this.http.get<Category[]>(this.apiGetCategories, {params});
  }
  getAllCategories(keyword: string, page:number, limit: number): Observable<any>{
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString())
    return this.http.get(this.apiGetAllCategories, {params});
  }
  deleteCategory(id: number): Observable<any>{
    return this.http.delete(this.apiGetCategories+"/"+id.toString());
  }
  getCategoryById(id: number): Observable<any>{
    return this.http.get(this.apiGetCategories+"/"+id.toString());
  }
  updateCategory(category: Category): Observable<any>{
    return this.http.put(this.apiGetCategories+"/"+category.id.toString(), category);
  }
}
