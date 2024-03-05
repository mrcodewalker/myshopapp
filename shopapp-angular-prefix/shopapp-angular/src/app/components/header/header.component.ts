import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {UserResponse} from "../../responses/user/user.response";
import {bottom} from "@popperjs/core";
import {NgbPopoverConfig} from "@ng-bootstrap/ng-bootstrap";
import {TokenService} from "../../services/token.service";
import {CartService} from "../../services/cart.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{
    userResponse?: UserResponse;
    isPopoverOpen = false;
    activeNavItem: number = 0;
    constructor(private userService: UserService,
                private popoverConfig: NgbPopoverConfig,
                private tokenService: TokenService,
                private cartService: CartService,
                private router: Router
                ) {}

  ngOnInit(): void {
      this.userResponse = this.userService.getUserResponseFromLocalStorage();

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
}
