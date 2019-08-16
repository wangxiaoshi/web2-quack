import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {Quack} from "../models/Quack";
import {QuackRService} from "../quackr.service";
import {User} from "../models/User";
import {Comment} from "../models/Comment";
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'quack-modal-content',
  templateUrl: './modal/quackShowModal.html',
  styleUrls: ['./quacks.component.css']
})
export class QuacksModalContent implements OnInit{
    id: number;
    title: string;
    text: string;
    date: Date;
    author: User;
    comments: Comment[];
    newComment: string;

    userLogged: boolean;
  constructor(public activeModal: NgbActiveModal, protected qService: QuackRService, protected authService: AuthenticationService) {}

  ngOnInit(){
    this.userLogged = this.authService.hasToken();
  }

  saveComment(e: Event): void {
    e.preventDefault();
    this.qService.createComment(this.id, this.newComment) .subscribe(
        () => {
                this.qService.getAllComments(this.id).subscribe(
                    comments => this.comments = comments
                );
                this.newComment = '';
        }
    )
  }

}

@Component({
  selector: 'app-quacks',
  templateUrl: './quacks.component.html',
  styleUrls: ['./quacks.component.css']
})
export class QuacksComponent implements OnInit {

    quacks: Quack[];

  constructor(private modalService: NgbModal, protected qService: QuackRService) { }

  ngOnInit() {
      this.qService.getAllQuacks().subscribe(
          quacks => this.quacks = quacks,
          console.error
      );
  }

  open(quack) {
    const modalRef = this.modalService.open(QuacksModalContent, { centered: true, size: 'lg' });
    modalRef.componentInstance.id = quack.id;
    modalRef.componentInstance.title = quack.title;
    modalRef.componentInstance.text = quack.text;
    modalRef.componentInstance.date = quack.date;
    modalRef.componentInstance.author = quack.author;

    this.qService.getAllComments(quack.id).subscribe(
        comments => modalRef.componentInstance.comments = comments,
        console.error
    )
  }

}
