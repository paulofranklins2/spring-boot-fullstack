import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthRequest} from "../../models/auth-request";
import {Observable} from "rxjs";
import {AuthResponse} from "../../models/auth-response";
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private authUrl = `${environment.api.baseUrl}${environment.api.authUrl}`;

    constructor(
        private http: HttpClient
    ) {
    }

    login(authRequest: AuthRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(
            this.authUrl, authRequest);
    }
}
