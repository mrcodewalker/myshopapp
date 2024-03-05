import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../environments/environment";
import {OrderDto} from "../dtos/order/order.dto";
import {Observable} from "rxjs";
import {OrderResponse} from "../responses/order/order.response";
import {Provinces} from "../models/provinces";
@Injectable({
  providedIn: 'root'
})
export  class ProvinceService{
  private getProvincesAPI : string = `${environment.apiBaseUrl}/users/provinces`;
  private getAllProvincesAPI: string = `${environment.apiBaseUrl}/users/get-provinces`;
  private getDistrictsAPI: string = `${environment.apiBaseUrl}/users/get-districts/`
  private getCommunesAPI: string = `${environment.apiBaseUrl}/users/get-communes/`
  constructor(private http: HttpClient) {
  }
  getProvinces(): Observable<Provinces[]>{
    return this.http.get<Provinces[]>(this.getProvincesAPI);
  }
  getAllProvinces(): Observable<any>{
    return this.http.get(this.getAllProvincesAPI);
  }
  getAllDistricts(provinceId: number): Observable<any>{
    return this.http.get(this.getDistrictsAPI+provinceId.toString());
  }
  getAllCommunes(districtId: number): Observable<any>{
    return this.http.get(this.getCommunesAPI+districtId.toString());
  }
}
