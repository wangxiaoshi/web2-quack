import {Component} from '@angular/core';
import {QuackRService} from "../quackr.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css'],
    providers: [QuackRService]
})
export class SignupComponent {

    public username: string = "";
    public email: string = "";
    public password: string = "";
    public passwordRepeat: string = "";

    public errorMessage: string;

    constructor(private qService: QuackRService, private router: Router) {
    }

    signUp(e: Event): void {
        e.preventDefault();
        this.errorMessage = null;

        if(this.username.trim() == null) {
            this.errorMessage = "Benutzername fehlt!";
            return;
        }
        if(this.email.trim() == null) {
            this.errorMessage = "E-Mailadresse fehlt!";
            return;
        }
        if(this.password.trim() == null) {
            this.errorMessage = "Passwort fehlt!";
            return;
        }

        if(this.password != this.passwordRepeat) {
            this.errorMessage = "Passwörter sind nicht gleich!";
            return;
        }


        this.qService.createUser(this.username, this.email, this.password).subscribe(
            () => this.router.navigate(['/login']),
            () => this.errorMessage = "Unzulässige Anmeldedaten!<br/>" +
                "Der Benutzername muss zwischen 6 und 18 Zeichen lang sein.<br/>" +
                "Die E-Mailadresse muss korrekt formatiert sein.<br/>" +
                "Das Passwort muss zwischen 6 und 16 Zeichen lang sein."
        )
    }

}
