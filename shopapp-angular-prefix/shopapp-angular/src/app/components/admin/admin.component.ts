import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {TokenService} from "../../services/token.service";
import {UserResponse} from "../../responses/user/user.response";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  userResponse?: UserResponse;

  constructor(
    private userService: UserService,
    private tokenService: TokenService,
    private router: Router,
  ) {
  }

  ngOnInit() {
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
    if (this.userResponse?.role.name.toUpperCase()=="USER"){
      alert("You don't have permission to access this page");
      window.history.back();
    }
    const currentAdminPage = localStorage.getItem('currentAdminPage');
    if (typeof currentAdminPage === 'string' && currentAdminPage !== null) {
      this.router.navigate([currentAdminPage]);
    }
  }

  logout() {
    this.userService.removeUserFromLocalStorage();
    this.tokenService.removeToken();
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
    this.router.navigate(['/login']);
  }

  showAdminComponent(componentName: string) {
    this.router.navigate([`/admin/${componentName}`]);
    localStorage.setItem('currentAdminPage', `/admin/${componentName}`);
  }
}
