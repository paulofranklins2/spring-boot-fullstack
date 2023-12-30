import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthResponse} from "../../models/auth-response";

@Injectable({
    providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {

    constructor() {
    }

    intercept(req: HttpRequest<any>,
              next: HttpHandler): Observable<HttpEvent<any>> {
        const storedAccess = localStorage.getItem('access_key');
        if (storedAccess) {
            const authResponse: AuthResponse = JSON.parse(storedAccess);
            const token = authResponse.token;
            if (token) {
                const authRequest = req.clone({
                    headers: new HttpHeaders({
                        Authorization: 'Bearer ' + token
                    })
                });
                return next.handle(authRequest);
            }
        }
        return next.handle(req);
    }
}
