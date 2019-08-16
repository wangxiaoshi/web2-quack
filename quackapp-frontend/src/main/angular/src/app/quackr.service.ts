import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ObjectUnsubscribedError, Observable} from "rxjs";
import {User} from "./models/User";
import {environment as env} from "../environments/environment";
import {map} from "rxjs/operators";
import {Injectable} from "@angular/core";
import {Quack} from "./models/Quack";
import {Comment} from "./models/Comment";
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class QuackRService {

    constructor(protected httpClient: HttpClient, protected authService: AuthenticationService) {
        this.httpClient = httpClient;
    }

    getQuack(id: number): Observable<Quack> {
        return this.httpClient.get<any[]>(`${env.apiUrl}/quacks/${id}`, {headers: this.authService.getAuthHeaders()}).pipe(
            map(obj => Quack.fromObject(obj))
        );
    }

    getAllQuacks(): Observable<Quack[]> {
        return this.httpClient.get<any[]>(`${env.apiUrl}/quacks`, {headers: this.authService.getAuthHeaders()}).pipe(
            map(objArr => objArr.map(obj => Quack.fromObject(obj)))
        );
    }

    createQuack(title: string, text: string, publicyVisible: boolean): Observable<Quack> {
        let data = {
            title: title,
            text: text,
            publiclyVisible: publicyVisible
        };

        return this.httpClient.post<any>(`${env.apiUrl}/quacks`, data, {headers: this.authService.getAuthHeaders()}).pipe(
            map(obj => Quack.fromObject(obj))
        );
    }

    updateQuack(id: number, title: string, text: string, publicyVisible: boolean): Observable<Quack> {
        let data = {
            title: title,
            text: text,
            publiclyVisible: publicyVisible
        };

        return this.httpClient.put<any>(`${env.apiUrl}/quacks/${id}`, data, {headers: this.authService.getAuthHeaders()}).pipe(
            map(obj => Quack.fromObject(obj))
        );
    }

    deleteQuack(quackId: number): Observable<Comment> {
        return this.httpClient.delete<any>(`${env.apiUrl}/quacks/${quackId}`, {headers: this.authService.getAuthHeaders()});
    }

    getUser(id: number): Observable<User> {
        return this.httpClient.get<any[]>(`${env.apiUrl}/users/${id}`, {headers: this.authService.getAuthHeaders()}).pipe(
            map(obj => User.fromObject(obj))
        );
    }

    getLoggedInUser(): Observable<User> {
        return this.httpClient.get<any[]>(`${env.apiUrl}/users/me`, {headers: this.authService.getAuthHeaders()}).pipe(
            map(obj => User.fromObject(obj))
        );
    }

    getAllUsers(): Observable<User[]> {
        return this.httpClient.get<any[]>(`${env.apiUrl}/users`, {headers: this.authService.getAuthHeaders()}).pipe(
            map(objArr => objArr.map(obj => User.fromObject(obj)))
        );
    }

    createUser(username: string, email: string, password: string): Observable<User> {
        let data = {
            username: username,
            email: email,
            passwordHash: password
        };

        return this.httpClient.post<any>(`${env.apiUrl}/users`, data, {headers: new HttpHeaders({'Content-Type':'application/json'})}).pipe(
            map(obj => User.fromObject(obj))
        );
    }

    updateUser(id: number, data: Object): Observable<User> {
        return this.httpClient.put<any>(`${env.apiUrl}/users/${id}`, data, {headers: new HttpHeaders({'Content-Type':'application/json'})}).pipe(
            map(obj => User.fromObject(obj))
        );
    }

    deleteUser(id: number): void {
        this.httpClient.delete(`${env.apiUrl}/users/${id}`, {headers: this.authService.getAuthHeaders()}).subscribe(
            result => console.log(result),
            err => console.error(err)
        );
    }

    getAllComments(quackId : number): Observable<Comment[]> {
        return this.httpClient.get<any[]>(`${env.apiUrl}/quacks/${quackId}/comments`, {headers: this.authService.getAuthHeaders()}).pipe(
            map(objArr => objArr.map(obj => Comment.fromObject(obj)))
        );
    }

    createComment(quackId: number, text: string): Observable<Comment> {
        let data = {
            qid: quackId,
            text: text
        };

        return this.httpClient.post<any>(`${env.apiUrl}/quacks/${quackId}/comments`, data, {headers: this.authService.getAuthHeaders()}).pipe(
            map(obj => Comment.fromObject(obj))
        );
    }

    deleteComment(quackId: number, commentId: number): Observable<Comment> {
        return this.httpClient.delete<any>(`${env.apiUrl}/quacks/${quackId}/comments/${commentId}`, {headers: this.authService.getAuthHeaders()});
    }
}
