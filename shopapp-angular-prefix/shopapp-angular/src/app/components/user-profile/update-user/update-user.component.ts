import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {TokenService} from "../../../services/token.service";
import {RegisterDto} from "../../../dtos/user/register.dto";
import {LoginDto} from "../../../dtos/user/login.dto";
import {log} from "ng-zorro-antd/core/logger";
import {LoginResponse} from "../../../responses/user/login.response";
import {UserResponse} from "../../../responses/user/user.response";

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.scss']
})
export class UpdateUserComponent implements OnInit{
  @ViewChild('updatedForm') updatedForm !: NgForm;
  userProfileForm: FormGroup;
  typeRequest: string ='';
  buttonHit: boolean = false;
  idEmail: number = 0;
  avatar: string = '';
  loginDto: LoginDto = {
    phone_number: '',
    password: '',
    email: ''
  }
  checkExistPhoneNumber: boolean = false;
  registerDto: RegisterDto = {
    fullname : '',
    phone_number : '',
    password : '',
    retype_password : '',
    address : '',
    date_of_birth: new Date(),
    role_id: 1,
    google_account_id: 0,
    facebook_account_id: 0,
    email: '',
    avatar: ''
  };
  userResponse?: UserResponse;
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private tokenService: TokenService,
    private route: ActivatedRoute
  ) {
    this.userProfileForm = this.formBuilder.group({
      fullname: [''],
      // email: ['', [Validators.email]],
      phone_number: ['', Validators.minLength(6)],
      password: ['', Validators.minLength(3)],
      retype_password: ['', Validators.minLength(3)],
      address: ['', [Validators.required, Validators.minLength(5)]],
      date_of_birth: [''],
    }, {
    });
  }
  ngOnInit() {
    debugger;
    this.route.queryParams.subscribe(params => {
      this.idEmail = params['id'];
      this.typeRequest = params['type'];
    });
  }

  save(){
    debugger;
    if (this.userProfileForm.get('phone_number')?.value.length<5){
      alert("Can not register this account because of invalid phone number");
      return;
    }
    if (this.userProfileForm.get('password')?.value.length<3){
      alert("Can not register this account because of invalid password");
      return;
    }
    if (this.typeRequest=="facebook"){
      this.registerDto.facebook_account_id = 1;
      this.userService.getFacebookUserInfo(this.idEmail).subscribe({
        next: (response: any) =>{
          debugger;
          this.registerDto.email = response.email;
          this.registerDto.fullname = "";
          this.registerDto.date_of_birth = this.userProfileForm.get('date_of_birth')?.value;
          this.registerDto.password = this.userProfileForm.get('password')?.value;
          this.registerDto.address = this.userProfileForm.get('address')?.value;
          this.registerDto.phone_number = this.userProfileForm.get('phone_number')?.value;
          this.registerDto.retype_password = this.registerDto.password;

          if (this.registerDto.phone_number.length>=6){
            this.userService.getUserByPhoneNumber(this.registerDto.phone_number).subscribe({
              next: (response: any) =>{
                debugger;
                if (response.id>0){
                  alert("This phone number has been used by someone please try by another phone number");
                }
              },
              complete: () =>{
                debugger;
              },
              error: (error: any) => {
                console.log("Error fetching data error: "+error.error.message);
              }
            });
          }
          this.userService.register(this.registerDto).subscribe({
            next: (response: any) =>{
              debugger;
              alert("You have been updated successfully! Please login again");
              this.loginDto.phone_number = this.registerDto.phone_number;
              this.loginDto.password = this.registerDto.password;
              this.loginDto.email = this.registerDto.email;
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
            },
            complete: () =>{
              debugger;
            },
            error: (error: any) => {
              console.log("Error fetching data error: "+error.error.message);
            }
          });
        },
        complete: () =>{
          debugger;
        },
        error: (error: any) => {
          console.log("Error fetching data error: "+error.error.message);
        }
      })
    } else {
      if (this.typeRequest=="email"){
        this.registerDto.google_account_id = 1;
        this.userService.getGoogleUserInfo(this.idEmail).subscribe({
          next: (response: any) =>{
            debugger;
            this.avatar = response.picture;
            this.registerDto.email = response.email;
            this.registerDto.fullname = "";
            this.registerDto.date_of_birth = this.userProfileForm.get('date_of_birth')?.value;
            this.registerDto.password = this.userProfileForm.get('password')?.value;
            this.registerDto.address = this.userProfileForm.get('address')?.value;
            this.registerDto.phone_number = this.userProfileForm.get('phone_number')?.value;
            this.registerDto.avatar = this.avatar;
            this.registerDto.retype_password = this.registerDto.password;
            if (this.registerDto.phone_number.length>=6){
              this.userService.getUserByPhoneNumber(this.registerDto.phone_number).subscribe({
                next: (response: any) =>{
                  debugger;
                  if (response.id>0){
                    alert("This phone number has been used by someone please try by another phone number");
                  }
                },
                complete: () =>{
                  debugger;
                },
                error: (error: any) => {
                  console.log("Error fetching data error: "+error.error.message);
                }
              });
            }
            this.userService.register(this.registerDto).subscribe({
              next: (response: any) =>{
                debugger;
                alert("You have been updated successfully! Please login again");
                this.loginDto.phone_number = this.registerDto.phone_number;
                this.loginDto.password = this.registerDto.password;
                this.loginDto.email = this.registerDto.email;
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
              },
              complete: () =>{
                debugger;
              },
              error: (error: any) => {
                console.log("Error fetching data error: "+error.error.message);
              }
            });
          },
          complete: () =>{
            debugger;
          },
          error: (error: any) => {
            console.log("Error fetching data error: "+error.error.message);
          }
        })
      }
    }
  }
  confirmPhoneNumber(){
    this.checkExistPhoneNumber=false;
    this.buttonHit = true;
    this.userService.getUserByPhoneNumber(this.userProfileForm.get('phone_number')?.value).subscribe({
      next: (response: any) =>{
        debugger;
        if (response.id>=1){
          this.checkExistPhoneNumber = true;
        } else {
          this.checkExistPhoneNumber = false;
        }
        console.log(this.checkExistPhoneNumber);
      },
      complete: () =>{
        debugger;
      },
      error: (error: any) =>{
        debugger;
        console.log("Error fetching data: error "+error.error.message);
      }
    })
  }

  protected readonly confirm = confirm;
}
