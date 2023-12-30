import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CustomerRegistrationRequest} from "../../models/customer-registration-request";

@Component({
    selector: 'app-manage-customer',
    templateUrl: './manage-customer.component.html',
    styleUrls: ['./manage-customer.component.scss']
})
export class ManageCustomerComponent {

    @Input()
    customer: CustomerRegistrationRequest = {};
    @Input()
    operation: 'create' | 'update' = 'create';
    title = 'Create Customer';
    @Output()
    cancel: EventEmitter<void> = new EventEmitter<void>();
    @Output()
    submit: EventEmitter<CustomerRegistrationRequest> =
        new EventEmitter<CustomerRegistrationRequest>();

    get isCustomerValid(): boolean {
        return this.hasLength(this.customer.name) &&
            this.hasLength(this.customer.email) &&
            this.customer.age !== undefined && this.customer.age >= 0 &&
            (
                this.operation === 'update' ||
                this.hasLength(this.customer.password) &&
                this.hasLength(this.customer.gender)
            );
    }

    private hasLength(input: string | undefined) {
        return input !== null && input !== undefined && input.length > 0

    }

    onSubmit() {
        this.submit.emit(this.customer)
    }

    onCancel() {
        this.cancel.emit();
    }
}
