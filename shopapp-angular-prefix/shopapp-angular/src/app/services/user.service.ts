import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {RegisterDto} from "../dtos/user/register.dto";
import {LoginDto} from "../dtos/user/login.dto";
import {environment} from "../environments/environment";
import {UserResponse} from "../responses/user/user.response";
import {UpdatedUserDto} from "../dtos/user/updated.user.dto";
import {TokenService} from "./token.service";
import {ChangesPasswordDto} from "../dtos/user/changes.password.dto";
import {UpdateUserByAdminDto} from "../dtos/user/update.user.by.admin.dto";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = `${environment.apiBaseUrl}/users/login`;
  private apiUserDetails = `${environment.apiBaseUrl}/users/details`;
  private apiUpdateUser = `${environment.apiBaseUrl}/users/details/`;
  private apiChangesPassword = `${environment.apiBaseUrl}/users/changes-password/`;
  private apiGetUsers = `${environment.apiBaseUrl}/users/get-users-by-keyword`;
  private apiUpdateUserByAdmin = `${environment.apiBaseUrl}/users/update-user/`;
  private apiUploadImage = `${environment.apiBaseUrl}/users/upload-image`;
  private apiConfig = {
    headers: this.createHeaders(),
  };
  constructor(private http: HttpClient,
              private tokenService: TokenService
              // private httpUtilService: HttpUtilService
              ) {}
  private createHeaders() : HttpHeaders {
    return new HttpHeaders({
      'Content-Type' : 'application/json',
      'Accept-Language' : 'vi'
    });
  }
  register(registerDto: RegisterDto):Observable<any> {
    this.createHeaders();
    return this.http.post(this.apiRegister, registerDto, this.apiConfig);
  }
  login(loginDto: LoginDto):Observable<any> {
    return this.http.post(this.apiLogin, loginDto, this.apiConfig );
  }
  getUserDetails(token: string): Observable<any>{
    return this.http.post(this.apiUserDetails, {
      headers: new HttpHeaders({
        'Content-Type' : 'application/json',
        Authorization: `Bearer ${token}`
      })
    });
  }
  saveUserResponseLocalStorage(userResponse?: UserResponse){
    try{
      debugger;
      if (userResponse==null || !userResponse){
        return;
      }
      const userResponseJSON = JSON.stringify(userResponse);

      localStorage.setItem('userResponse', userResponseJSON);

      console.log('User response saved to local storage');
    } catch (error){
      console.error('Error saving user response to local storage: ',error);
    }
  }
  removeUserFromLocalStorage():void{
    try {
      localStorage.removeItem('userResponse');
      console.log("UserResponse data removed from local storage");
    } catch (error){
      console.log("Error removing user data from local storage: ",error);
    }
  }
  getUserResponseFromLocalStorage(): UserResponse | undefined {
    try {
      const userResponseJSON = localStorage.getItem('userResponse');

      if (userResponseJSON === null || userResponseJSON === undefined) {
        return undefined;
      }

      // Parse the JSON to userResponse
      const userResponse = JSON.parse(userResponseJSON);
      return userResponse;
    } catch (error) {
      console.error('Error getting user response from local storage: ', error);
      return undefined;
    }
  }
  logOut(){

  }
  updateUserDetail(token: string, updatedUser: UpdatedUserDto, file: File): Observable<any> {
    const formData = new FormData();
    if (file) {
      formData.append('file', file);
    } else {
      updatedUser.avatar = "default_avatar.png";
    }
    debugger;
    formData.append('updatedUser', JSON.stringify(updatedUser));

    let userResponse = this.getUserResponseFromLocalStorage();
    return this.http.put(this.apiUpdateUser + userResponse?.id, formData, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`
      })
    });
  }
  changesPasswordUser(token: string, changesPassword: ChangesPasswordDto): Observable<any>{
    debugger;
    let userResponse = this.getUserResponseFromLocalStorage();
    return this.http.post(this.apiChangesPassword+userResponse?.id, changesPassword, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      })
    });
  }
  getAllUsers(keyword: string, page: number, limit: number) :Observable<any> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get(this.apiGetUsers, {params});
  }
  updateUserByAdmin(id: number, user: UpdateUserByAdminDto):Observable<any>{
    return this.http.put(this.apiUpdateUserByAdmin+id.toString(), user);
  }
}
