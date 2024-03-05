import {inject, Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateFn} from "@angular/router";
import { TokenService } from "../services/token.service";
import {UserService} from "../services/user.service";
import {UserResponse} from "../responses/user/user.response";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard {
  userResponse?: UserResponse;
  constructor(private tokenService: TokenService,
              private router: Router,
              private userService: UserService) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    debugger;
    const token = this.tokenService.getToken();
    const isUserValid = this.tokenService.getUserId()>0;
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
    const isAdmin = this.userResponse?.role.name == 'admin';
    debugger;
    if ((!token || this.tokenService.isTokenExpired()) && !isUserValid && !isAdmin) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
export const AdminGuardFn: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot): boolean => {
  debugger
  return inject(AdminGuard).canActivate(next, state);
}
