import {Component} from '@angular/core';
import {User} from "../models/User";
import {Profile} from "../profile-view/Profile";
import {ActivatedRoute, Router} from "@angular/router";
import {QuackRService} from "../quackr.service";

@Component({
    selector: 'app-profile-edit',
    templateUrl: './profile-edit.component.html',
    styleUrls: ['./profile-edit.component.css']
})
export class ProfileEditComponent {

    profile: Profile;
    user: User;

    newUsername: string;
    newRealName: string;
    newEmail: string;
    newPassword: string;
    newPasswordRepeat: string;

    errorMessage: string;

    success: boolean = false;

    constructor(private router: Router, private route: ActivatedRoute, protected qService: QuackRService) {
        this.route.queryParams.subscribe(params => {
            let id = +params['id'] || 0;
            if (id > 0) {
                this.loadUserProfile(id);
            }
        });
    }

    private loadUserProfile(id: number): void {
        this.qService.getUser(id).subscribe(
            user => {
                this.user = user;
                this.profile = Profile.fromUser(user);
            },
            console.error
        );
    }

    saveProfile(e: Event): void {
        e.preventDefault();
        this.errorMessage = null;
        this.success = false;

        let data = {
            id: this.user.id,
            username: this.user.username,
            email: this.user.email,
            realName: this.user.realName,
            passwordHash: this.user.passwordHash
        };

        if (this.newUsername != null && this.newUsername.trim() != '') {
            data.username = this.newUsername;
        }
        if (this.newEmail != null && this.newEmail.trim() != '') {
            data.email = this.newEmail;
        }
        if (this.newRealName != null && this.newRealName.trim() != '') {
            data.realName = this.newRealName;
        }
        if (this.newPassword != null && this.newPassword != '') {
            if(this.newPassword == this.newPasswordRepeat) {
                data.passwordHash = this.newPassword;
            } else {
                this.errorMessage = "Die eingegebenen Passwörter sind nicht gleich!";
                return;
            }
        }

        this.qService.updateUser(this.user.id, data).subscribe(
            () => this.success = true,
            () => this.errorMessage = "Fehler bei der Eingabe. Die Daten wurden nicht gespeichert."
        );
    }

    deleteProfile(): void {
        this.qService.deleteUser(this.user.id);
    }

}
