import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {CustomerRegistrationRequest} from "../../models/customer-registration-request";
import {CustomerService} from "../../services/customer/customer.service";
import {AuthService} from "../../services/auth/auth.service";
import {AuthRequest} from "../../models/auth-request";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
    errorMsg = '';

    customer: CustomerRegistrationRequest = {};


    constructor(
        private router: Router,
        private customerService: CustomerService,
        private authService: AuthService
    ) {
    }

    login() {
        this.router.navigate(['login']);
    }

    createAccount() {
        this.customerService.saveCustomer(this.customer)
            .subscribe({
                next: () => {
                    const authRequest: AuthRequest = {
                        username: this.customer.email,
                        password: this.customer.password
                    }
                    this.authService.login(authRequest)
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
                    this.router.navigate(['customers'])
                }
            })
    }
}
