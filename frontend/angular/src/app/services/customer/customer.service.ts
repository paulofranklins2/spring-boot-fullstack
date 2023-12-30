import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CustomerDTO} from "../../models/customerDTO";
import {environment} from "../../../environments/environment";
import {CustomerRegistrationRequest} from "../../models/customer-registration-request";
import {CustomerUpdateRequest} from "../../models/customer-update-request";

@Injectable({
    providedIn: 'root'
})
export class CustomerService {

    private customerUrl = `${environment.api.baseUrl}${environment.api.customerUrl}`;

    constructor(
        private http: HttpClient
    ) {
    }

    getAllCustomers(): Observable<CustomerDTO[]> {
        return this.http.get<CustomerDTO[]>(
            this.customerUrl
        );
    }

    saveCustomer(customer: CustomerRegistrationRequest): Observable<void> {
        return this.http.post<void>(
            this.customerUrl, customer
        );
    }

    deleteCustomer(id: number | undefined): Observable<void> {
        return this.http.delete<void>(
            `${this.customerUrl}/${id}`,
        );
    }

    updateCustomer(id: number | undefined,
                   customer: CustomerUpdateRequest): Observable<void> {
        return this.http.put<void>(
            `${this.customerUrl}/${id}`, customer
        );
    }
}
