import {Component} from '@angular/core';
import {AuthRequest} from "../../models/auth-request";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent {
    authRequest: AuthRequest = {};
    errorMsg = '';

    constructor(
        private authService: AuthService,
        private router: Router
    ) {
    }

    login() {
        this.errorMsg = '';
        this.authService.login(this.authRequest)
            .subscribe({
                next: (authResponse) => {
                    console.log(authResponse);
                    localStorage.setItem('access_key', JSON.stringify(authResponse));
                    this.router.navigate(['customers'])

                },
                error: (err) => {
                    if (err.error.statusCode === 401) {
                        this.errorMsg = 'Email or Password is incorrect.'
                    }
                    console.log();
                }
            });
    }

    signUp() {
        this.router.navigate(['register']);
    }
}
