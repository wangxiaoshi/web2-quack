import { Component, OnInit } from '@angular/core';
import {QuackRService} from "../quackr.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-delete-quack',
  templateUrl: './delete-quack.component.html',
  styleUrls: ['./delete-quack.component.css']
})
export class DeleteQuackComponent implements OnInit {

  constructor(private qService: QuackRService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
      this.qService.deleteQuack(this.route.snapshot.params.id).subscribe(
          () => this.router.navigate(['/admin-panel']),
      );
  }

}
