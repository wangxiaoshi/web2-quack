import { Component, OnInit } from '@angular/core';
import {Quack} from "../models/Quack";
import {User} from "../models/User";
import {QuackRService} from "../quackr.service";
import {Profile} from "../profile-view/Profile";

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent implements OnInit {

    quacks: Quack[];
    userProfiles: Profile[];

  constructor(protected qService: QuackRService) { }

  ngOnInit() {
      this.qService.getAllQuacks().subscribe(
          quacks => this.quacks = quacks,
          console.error
      );
      this.qService.getAllUsers().subscribe(
          users => this.userProfiles = users.map(
              user => Profile.fromUser(user)
          ),
          console.error
      );
  }

}
