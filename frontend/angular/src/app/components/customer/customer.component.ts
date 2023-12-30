import {Component, OnInit} from '@angular/core';
import {CustomerDTO} from "../../models/customerDTO";
import {CustomerService} from "../../services/customer/customer.service";
import {CustomerRegistrationRequest} from "../../models/customer-registration-request";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
    selector: 'app-customer',
    templateUrl: './customer.component.html',
    styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {
    showDisplay: boolean = false;
    customers: CustomerDTO[] = [];
    customer: CustomerRegistrationRequest = {};
    operation: 'create' | 'update' = 'create';


    constructor(
        private customerService: CustomerService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) {
    }

    ngOnInit(): void {
        this.fetchAllCustomers();
    }

    private fetchAllCustomers() {
        this.customerService.getAllCustomers()
            .subscribe({
                next: (data) => {
                    this.customers = data;
                }
            })
    }

    save(customer: CustomerRegistrationRequest) {
        if (customer) {
            if (this.operation === 'create') {
                this.customerService.saveCustomer(customer)
                    .subscribe({
                        next: () => {
                            this.fetchAllCustomers();
                            this.showDisplay = false;
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Customer Created',
                                detail: `${customer.name}, was successfully created.`
                            });
                            this.customer = {};
                        }
                    });
            } else if (this.operation === 'update') {
                this.customerService.updateCustomer(customer.id, customer)
                    .subscribe({
                        next: () => {
                            this.fetchAllCustomers();
                            this.showDisplay = false;
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Customer Updated',
                                detail: `${customer.name}, was successfully updated.`
                            });
                            this.customer = {};
                        }
                    });
            }
        }
    }

    deleteCustomer(customer: CustomerDTO) {
        this.confirmationService.confirm({
            header: 'Delete Customer',
            message: `Are you sure you want to delete
            ${customer.name}? You can\'t undo this action afterwords`,
            accept: () => {
                this.customerService.deleteCustomer(customer.id)
                    .subscribe({
                        next: () => {
                            this.fetchAllCustomers();
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Customer Deleted',
                                detail: `${customer.name}, was successfully deleted.`
                            });
                        }
                    });
            }
        });
    }

    updateCustomer(customerDTO: CustomerDTO) {
        this.showDisplay = true;
        this.customer = customerDTO;
        this.operation = 'update';
    }

    createCustomer() {
        this.showDisplay = true
        this.customer = {};
        this.operation = 'create';
    }

    cancel(): void {
        this.showDisplay = false
        this.customer = {};
    }
}
