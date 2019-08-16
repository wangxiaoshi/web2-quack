import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, of} from "rxjs";
import { environment as env } from '../environments/environment';
import {map} from "rxjs/operators";

@Injectable()
export class AuthenticationService {

    private token: string;

    constructor(protected httpClient: HttpClient) {
        this.httpClient = httpClient;
    }

    requestToken(username: string, password: string): Observable<boolean> {
      return this.httpClient.post(`${env.apiUrl}/auth`, {username, password}, {headers: this.getAuthHeaders(), responseType: 'text'})
            .pipe(map(obj => {
                this.token = obj;
                return true;
            }));
    }

    deleteToken(): Observable<boolean> {
        this.token = null;
        return of(true);
    }

    getAuthHeaders(): HttpHeaders {
        return this.token == null ? new HttpHeaders({'Content-Type':'application/json'}) : new HttpHeaders({
            'Authorization': `Bearer ${this.token}`,
            'Content-Type':'application/json'
        });
    }

    hasToken(): boolean {
        return this.token != null;
    }

}
