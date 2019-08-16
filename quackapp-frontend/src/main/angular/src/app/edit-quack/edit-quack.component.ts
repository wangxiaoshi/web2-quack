import { Component, OnInit } from '@angular/core';
import {Quack} from "../models/Quack";
import {QuackRService} from "../quackr.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-quack',
  templateUrl: './edit-quack.component.html',
  styleUrls: ['./edit-quack.component.css']
})
export class EditQuackComponent implements OnInit {

    public id: number;
    public title: string = "";
    public text: string = "";
    public publiclyVisible: boolean = true;

    public errorMessage: string;

  quack: Quack;

  constructor(private router: Router, private route: ActivatedRoute, private modalService: NgbModal, protected qService: QuackRService) { }

  ngOnInit() {
      this.id = this.route.snapshot.params.id;
      this.qService.getQuack(this.route.snapshot.params.id).subscribe(
          quack => {
              this.quack = quack,
              this.title = quack.title;
              this.text = quack.text;
              this.publiclyVisible = quack.publiclyVisible;
          },
          console.error
      );
  }

  edit(e: Event): void {
      e.preventDefault();
      this.errorMessage = null;

      this.qService.updateQuack(this.id, this.title, this.text, this.publiclyVisible).subscribe(
          () => this.router.navigate(['/admin-panel']),
      );
  }

}
