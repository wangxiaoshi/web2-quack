import { Component } from '@angular/core';
import {AuthenticationService} from "../../authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(private authService: AuthenticationService, private router: Router) { }

  isUserLoggedIn() : boolean {
      return this.authService.hasToken();
  }

  userLogout(): void {
      this.authService.deleteToken();
      this.router.navigate(['/home']);
  }

}
