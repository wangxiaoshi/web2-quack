import { Component, OnInit } from '@angular/core';
import {QuackRService} from "../quackr.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent {
    public title: string = "";
    public text: string = "";
    public publiclyVisible: boolean = true;

    public errorMessage: string;
    


    constructor(private qService: QuackRService, private router: Router) {
    }

    quack(e: Event): void {
        e.preventDefault();
        this.errorMessage = null;

        this.qService.createQuack(this.title, this.text, this.publiclyVisible).subscribe(
            () => this.router.navigate(['/']),
            () => this.errorMessage = "Ein Fehler ist aufgetreten! Dein Quack wurde nicht gepostet.<br/> " +
                "Beachte, dass du zum Quacken eingeloggt sein musst. Außerdem muss der Titel zwischen 3 und 20 Zeichen " +
                "lang sein und der Text muss zwischen 6 und 300 Zeichen lang sein."
        )
    }
}
