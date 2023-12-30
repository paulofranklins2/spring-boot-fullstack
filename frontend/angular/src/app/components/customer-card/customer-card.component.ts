import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CustomerDTO} from "../../models/customerDTO";

@Component({
    selector: 'app-customer-card',
    templateUrl: './customer-card.component.html',
    styleUrls: ['./customer-card.component.scss']
})
export class CustomerCardComponent {

    @Input()
    customer: CustomerDTO = {}
    @Input()
    customerIndex = 0;
    @Output()
    update: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();
    @Output()
    delete: EventEmitter<CustomerDTO> = new EventEmitter<CustomerDTO>();

    get customerImage(): string {
        let gender = this.customer.gender === 'MALE' ? 'men' : 'women'
        return `https://randomuser.me/api/portraits/${gender}/${this.customerIndex}.jpg`
    }

    onDelete() {
        this.delete.emit(this.customer)
    }

    onUpdate() {
        this.update.emit(this.customer)
    }
}
