import {inject, Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateFn} from "@angular/router";
import { TokenService } from "../services/token.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(private tokenService: TokenService, private router: Router) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    debugger;
    const token = this.tokenService.getToken();
    const isUserValid = this.tokenService.getUserId()>0;
    if ((!token || this.tokenService.isTokenExpired()) && !isUserValid) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
export const AuthGuardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  debugger
  return inject(AuthGuard).canActivate(next, state);
}
