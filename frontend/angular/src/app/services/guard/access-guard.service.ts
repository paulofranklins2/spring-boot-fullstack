import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {AuthResponse} from "../../models/auth-response";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
    providedIn: 'root'
})
export class AccessGuardService implements CanActivate {

    constructor(
        private route: Router
    ) {
    }

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<
        boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

        const storedAccess = localStorage.getItem('access_key');
        if (storedAccess) {
            const authResponse: AuthResponse = JSON.parse(storedAccess);
            const token = authResponse.token;
            if (token) {
                const jwtHelper = new JwtHelperService();
                const isTokenNonExpired = !jwtHelper.isTokenExpired(token)
                if (isTokenNonExpired) {
                    return true;
                }
            }
        }
        this.route.navigate(['login']);
        return false;
    }
}
