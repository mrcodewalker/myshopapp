import {Component, OnInit, ViewChild} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  NgForm,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {TokenService} from "../../services/token.service";
import {UserResponse} from "../../responses/user/user.response";
import {UpdatedUserDto} from "../../dtos/user/updated.user.dto";

@Component({
  selector: 'user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit{
  @ViewChild('updatedForm') updatedForm !: NgForm;
  userProfileForm: FormGroup;
  dateOfBirthFirst?: Date;
  userResponse?: UserResponse;
  token: string = this.tokenService.getToken() ?? '';
  password: string = '';
  retype_password: string ='';
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private tokenService: TokenService
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
      validators: this.passwordMatchValidators
    });
  }
  passwordMatchValidators(): ValidatorFn {
    return (formGroup: AbstractControl): ValidationErrors | null => {
      const password = formGroup.get('password')?.value;
      const retypedPassword = formGroup.get('retype_password')?.value;
      if (password !== retypedPassword){
        return { passwordMismatch: true };
        debugger;
      }
      return null;
    };
  }
  ngOnInit() {
    debugger;
    this.userService.getUserDetails(this.token).subscribe({
      next: (response: any) => {
      debugger;
      this.userResponse = {
        ...response,
        date_of_birth: new Date(response.date_of_birth)
      };
      const dateOfBirth = this.userResponse?.date_of_birth ? new Date(this.userResponse.date_of_birth) : null;
        debugger;
      this.userProfileForm.patchValue({
        fullname: this.userResponse?.fullname ?? '',
        address: this.userResponse?.address ?? '',
        date_of_birth: dateOfBirth,
        phone_number: this.userResponse?.phone_number ?? '',
      });
      this.userService.saveUserResponseLocalStorage(this.userResponse);
    },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        alert(error.error.message);
      }
    })
  }
  checkPasswordMatch(){
    if (this.password!==this.retype_password){
      this.updatedForm.form.controls['retype_password'].setErrors({'passwordMismatch': true});
    } else {
      this.updatedForm.form.controls['retype_password'].setErrors(null);
    }
  }
  save(){
    debugger;
    if (this.userProfileForm.valid) {
      const updatedUserDTO: UpdatedUserDto = {
        fullname: this.userProfileForm.get('fullname')?.value,
        address: this.userProfileForm.get('address')?.value,
        date_of_birth: this.userProfileForm.get('date_of_birth')?.value,
      };
      debugger;
      this.userService.updateUserDetail(this.token, updatedUserDTO).subscribe({
        next: (response: any) => {
          alert("You have changed information successfully!")
          this.router.navigate(['/']);
        },
        error: (error: any) => {
          alert(error.error.message);
        }
      });
    } else {
      if (this.userProfileForm.hasError('passwordMismatch')){
        alert('Password and retype password is not correctly');
      }
    }
  }
  saveChanges(){

  }

}
