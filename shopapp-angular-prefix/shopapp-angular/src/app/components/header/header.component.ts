import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {UserResponse} from "../../responses/user/user.response";
import {bottom} from "@popperjs/core";
import {NgbPopoverConfig} from "@ng-bootstrap/ng-bootstrap";
import {TokenService} from "../../services/token.service";
import {CartService} from "../../services/cart.service";
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../environments/environment";
import {LoginGoogleDto} from "../../dtos/user/login.google.dto";
import {LoginFacebookDto} from "../../dtos/user/login.facebook.dto";
import {LoginDto} from "../../dtos/user/login.dto";
import {LoginResponse} from "../../responses/user/login.response";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{
    userResponse?: UserResponse;
    isPopoverOpen = false;
    activeNavItem: number = 0;
    email: string = '';
  googleInfoLogin: LoginGoogleDto = {
    email: '',
    picture: '',
    name: ''
  };
  facebookInfoLogin: LoginFacebookDto = {
    email: '',
    facebook_id: '',
    name: ''
  }
  loginDto: LoginDto = {
    phone_number: '',
    password: '',
    email: ''
  }
  typeRequest: string ='';
  idEmail: number = 0;
    avatar?: string = '';
    token: string = this.tokenService.getToken() ?? '';
    name: string = '';
    constructor(private userService: UserService,
                private popoverConfig: NgbPopoverConfig,
                private tokenService: TokenService,
                private cartService: CartService,
                private route: ActivatedRoute,
                private router: Router
                ) {}

  ngOnInit(): void {
      debugger;
      localStorage.removeItem('codewalker');
    this.route.queryParams.subscribe(params => {
      this.idEmail = params['id'];
      this.typeRequest = params['type'];
    });

    if (this.typeRequest=="facebook"){
      this.userService.getFacebookUserInfo(this.idEmail).subscribe({
        next: (response: any) =>{
          this.email = response.email;
          this.userService.getPhoneNumber(this.email).subscribe({
            next: (clone: any) =>{
              if (clone.phone_number==""||clone.phone_number.length==0){
                this.router.navigate(['/users/update'], { queryParams: { id: this.idEmail, type: this.typeRequest }});
              } else {
                this.loginDto.phone_number = clone.phone_number;
                this.loginDto.email = this.email;
                this.loginDto.password = "09042006";
                this.userService.login(this.loginDto)
                  .subscribe({
                      next: (response: LoginResponse) => {
                        debugger
                        const {token} = response
                          this.tokenService.setToken(token);
                          debugger;
                          this.userService.getUserDetails(token).subscribe({
                            next: (userDetails: any) => {
                              debugger;
                              this.userResponse = {
                                ...userDetails,
                                date_of_birth: new Date(userDetails.date_of_birth)
                              };
                              let avatarUrl = userDetails.avatar;
                              let searchString = "https://lh3.googleusercontent.com";
                              if (avatarUrl.substring(0, searchString.length)===searchString){
                                this.avatar = userDetails.avatar;
                              } else {
                                this.avatar = `${environment.apiBaseUrl}/products/images/${userDetails.avatar}`;
                              }
                              this.userService.saveUserResponseLocalStorage(this.userResponse);
                              if (this.userResponse?.role.name == 'admin'){
                                this.router.navigate(['/admin']);
                              } else if (this.userResponse?.role.name == 'user') {
                                this.router.navigate(['/']);
                              }
                            },
                            complete: () => {
                              debugger
                            },
                            error: (error : any) => {
                              debugger
                            }
                          })
                      },
                      complete: () => {
                        debugger
                      },
                      error: (error : any) => {
                        debugger
                      }
                    }
                  );
              }
            },
            complete: () =>{
              debugger;
            },
            error: (error: any) => {
              debugger;
              console.log("Error fetching data: "+error.error.message);
            }
          })
        },
        complete: () =>{
          debugger;
        },
        error: (error: any) =>{
          debugger;
          console.log("Error fetching data: "+error.error.message);
        }
      })
    }
    // console.log("DAY LA HEADER: "+this.idEmail+" "+this.typeRequest);
    if (this.typeRequest=="email"){
      this.userService.getGoogleUserInfo(this.idEmail).subscribe({
        next: (response: any) =>{
          this.email = response.email;
          this.avatar = response.picture;
          this.userService.getPhoneNumber(this.email).subscribe({
            next: (clone: any) =>{
              if (clone.phone_number==""||clone.phone_number.length==0){
                this.router.navigate(['/users/update'], { queryParams: { id: this.idEmail, type: this.typeRequest }});
              } else {
                this.loginDto.phone_number = clone.phone_number;
                this.loginDto.email = this.email;
                this.loginDto.password = "09042006";
                this.userService.login(this.loginDto)
                  .subscribe({
                      next: (response: LoginResponse) => {
                        debugger
                        const {token} = response
                        this.tokenService.setToken(token);
                        debugger;
                        this.userService.getUserDetails(token).subscribe({
                          next: (userDetails: any) => {
                            debugger;
                            this.userResponse = {
                              ...userDetails,
                              date_of_birth: new Date(userDetails.date_of_birth)
                            };
                            let avatarUrl = userDetails.avatar;
                            let searchString = "https://lh3.googleusercontent.com";
                            if (avatarUrl.substring(0, searchString.length)===searchString){
                              this.avatar = userDetails.avatar;
                            } else {
                              this.avatar = `${environment.apiBaseUrl}/products/images/${userDetails.avatar}`;
                            }
                            this.userService.saveUserResponseLocalStorage(this.userResponse);
                            if (this.userResponse?.role.name == 'admin'){
                              this.router.navigate(['/admin']);
                            } else if (this.userResponse?.role.name == 'user') {
                              this.router.navigate(['/']);
                            }
                          },
                          complete: () => {
                            debugger
                          },
                          error: (error : any) => {
                            debugger
                          }
                        })
                      },
                      complete: () => {
                        debugger
                      },
                      error: (error : any) => {
                        debugger
                      }
                    }
                  );
              }
            },
            complete: () =>{
              debugger;
            },
            error: (error: any) => {
              debugger;
              console.log("Error fetching data: "+error.error.message);
            }
          })
        },
        complete: () =>{
          debugger;
        },
        error: (error: any) =>{
          debugger;
          console.log("Error fetching data: "+error.error.message);
        }
      })
    }
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
    this.userService.getUserDetails(this.token).subscribe({
      next: (response: any) =>{
        debugger;
        let avatarUrl = response.avatar;
        let searchString = "https://lh3.googleusercontent.com";
        if (avatarUrl.substring(0, searchString.length)===searchString){
          this.avatar = response.avatar;
        } else {
          this.avatar = `${environment.apiBaseUrl}/products/images/${response.avatar}`;
        }
      },
      complete: () =>{
        debugger;
      },
      error: (error: any) =>{
        debugger;
        console.log("Error fetching data error: "+error.error.message);
      }
    })
    }

  protected readonly bottom = bottom;
  togglePopover(event: Event){
    event.preventDefault();
    this.isPopoverOpen = !this.isPopoverOpen;
  }
  handleItemClick(index: number){
    if (index===0){
      this.router.navigate(['/user-profile']);
    } else {
      if (index === 3) {
        this.userService.removeUserFromLocalStorage();
        this.tokenService.removeToken();
        this.userResponse = this.userService.getUserResponseFromLocalStorage();
        // this.cartService.clearCart();
      } else {
        if (index === 2){
          this.router.navigate(['/changes-password']);
        } else {
          if (index === 1){
            this.router.navigate(['/my-purchase']);
          }
        }
      }
    }
      this.isPopoverOpen = false;
  }
  setActiveNavItem(index: number){
    this.activeNavItem = index;
  }
  updateAccount(){

  }
}
