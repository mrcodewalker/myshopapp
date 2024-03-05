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
import {UpdatedUserDto} from "../../dtos/user/updated.user.dto";
import {ChangesPasswordDto} from "../../dtos/user/changes.password.dto";

@Component({
  selector: 'app-changes-password',
  templateUrl: './changes-password.component.html',
  styleUrls: ['./changes-password.component.scss']
})
export class ChangesPasswordComponent implements OnInit{
  @ViewChild('passwordForm') passwordForm !: NgForm;
  changesPasswordForm: FormGroup;
  token: string = this.tokenService.getToken() ?? '';
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private tokenService: TokenService
  ) {
    this.changesPasswordForm = this.formBuilder.group({
      old_password: ['',Validators.minLength(3)],
      password: ['', Validators.minLength(3)],
      retype_password: ['', Validators.minLength(3)],
    }, {
      validators: this.passwordMatchValidators
    });
  }
  ngOnInit() {

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
  changesPassword(){
    debugger;
    if (this.changesPasswordForm.get('password')?.value === this.changesPasswordForm.get('old_password')?.value){
      alert("Can not changes password with a same value");
      return;
    }
    if (this.changesPasswordForm.get('password')?.value ===''&&this.changesPasswordForm.get('retype_password')?.value===''){
      alert("New password can not is empty value");
      return;
    }
    if (this.changesPasswordForm.get('password')?.value.length < 3){
      alert('Password length must be greater or equals than 3');
      return;
    }
    if (this.changesPasswordForm.get('retype_password')?.value.length < 3){
      alert('Retype password length must be greater or equals than 3');
      return;
    }
    if (this.changesPasswordForm.get('password')?.value !== this.changesPasswordForm.get('retype_password')?.value){
      alert('Password and retype password does not match');
      return;
    }
    if (this.changesPasswordForm.valid) {
      const changesPasswordForm: ChangesPasswordDto = {
        password: this.changesPasswordForm.get('password')?.value,
        retype_password: this.changesPasswordForm.get('retype_password')?.value,
        old_password: this.changesPasswordForm.get('old_password')?.value,
      };
      debugger;
      this.userService.changesPasswordUser(this.token, changesPasswordForm).subscribe({
        next: (response: any) => {
          this.userService.removeUserFromLocalStorage();
          this.tokenService.removeToken();
          this.router.navigate(['/login']);
          alert("You have been changed password successfully, please log in again");
        },
        error: (error: any) => {
          alert(error.error.message);
        }
      });
    } else {
      if (this.changesPasswordForm.hasError('passwordMismatch')){
        alert('Password and retype password is not correctly');
      }
    }
  }
}
