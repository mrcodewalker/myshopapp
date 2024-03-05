import {Component, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {RegisterDto} from "../../dtos/user/register.dto";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm !: NgForm;
  phoneNumber: string;
    fullName: string;
    password: string;
    retypePassword: string;
    address: string;
    dateOfBirth: Date;
    isAccepted: boolean
    constructor(private router: Router, private userService: UserService) {
      this.phoneNumber = '';
      this.fullName = '';
      this.password='';
      this.retypePassword='';
      this.address='';
      this.dateOfBirth = new Date();
      this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
      this.isAccepted = false;
      // dependency injection
    }

    register(){
      const registerDto: RegisterDto = {
        "fullname":this.fullName,
        "phone_number":this.phoneNumber,
        "address":this.address,
        "password":this.password,
        "retype_password":this.retypePassword,
        "date_of_birth":this.dateOfBirth,
        "facebook_account_id":0,
        "google_account_id":0,
        "role_id":1
      }

      this.userService.register(registerDto)
        .subscribe({
            next: (response: any) => {
              debugger
              if (response && (response.status === 200 || response.status === 201)) {
                this.router.navigate(['/login']);
              } else {

              }
            },
            complete: () => {
              debugger
            },
          error: (error : any) => {
              alert(`Cannot register, error: ${error.error}`);
              debugger
            console.error('Register not accepted: ',error);
          }
          }
        );
    }
    onPhoneChange(){
      console.log(this.phoneNumber)
    }
    validatePhoneNumber(){
      if (this.phoneNumber.length<6){

      }
    }
    checkPasswordMatch(){
      if (this.password!==this.retypePassword){
        this.registerForm.form.controls['retypePassword'].setErrors({'passwordMismatch': true});
      } else {
        this.registerForm.form.controls['retypePassword'].setErrors(null);
      }
    }
    checkAge(){
      if (this.dateOfBirth){
        const today = new Date();
        const birthDate = new Date(this.dateOfBirth);
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        if (monthDiff<0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())){
          age--;
        }

        if (age<18){
          this.registerForm.form.controls['dateOfBirth'].setErrors({'invalidAge':true});
        } else {
          this.registerForm.form.controls['dateOfBirth'].setErrors(null);
        }
      }
    }
    checkAcceptedTerms(){
      if (!this.isAccepted){
        this.registerForm.form.controls['isAccepted'].setErrors({'invalidTerms':true});
      } else {
        this.registerForm.form.controls['isAccepted'].setErrors(null);
      }
    }
}
