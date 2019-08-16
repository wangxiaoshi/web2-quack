import {Component} from '@angular/core';
import {AuthenticationService} from "../authentication.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    public username: string = "";
    public password: string = "";

    errorMessage: string;

    constructor(private authService: AuthenticationService, private router: Router) {
    }

    login(e: Event): void {
        e.preventDefault();
        this.errorMessage = null;
        this.authService.requestToken(this.username, this.password).subscribe(
            () => this.router.navigate(['/home']),
            () => this.errorMessage = "Fehlerhafte Anmeldedaten!"
        );
    }

}
