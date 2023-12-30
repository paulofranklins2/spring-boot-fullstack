import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import {InputTextModule} from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { MenuModule } from 'primeng/menu';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SidebarModule } from 'primeng/sidebar';
import { MessageModule } from 'primeng/message';
import { CardModule } from 'primeng/card';
import { BadgeModule } from 'primeng/badge';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import {CustomerComponent} from "./components/customer/customer.component";
import {HeaderBarComponent} from "./components/header-bar/header-bar.component";
import {MenuBarComponent} from "./components/menu-bar/menu-bar.component";
import {MenuItemComponent} from "./components/menu-item/menu-item.component";
import {AvatarModule} from "primeng/avatar";
import { ManageCustomerComponent } from './components/manage-customer/manage-customer.component';
import {DropdownModule} from "primeng/dropdown";
import {DragDropModule} from "primeng/dragdrop";
import { LoginComponent } from './components/login/login.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HttpInterceptorService} from "./services/interceptor/http-interceptor.service";
import { CustomerCardComponent } from './components/customer-card/customer-card.component';
import {ConfirmationService, MessageService} from "primeng/api";
import { RegisterComponent } from './components/register/register.component';

@NgModule({
    declarations: [
        AppComponent,
        CustomerComponent,
        HeaderBarComponent,
        MenuBarComponent,
        MenuItemComponent,
        ManageCustomerComponent,
        LoginComponent,
        CustomerCardComponent,
        RegisterComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        FormsModule,
        InputTextModule,
        ButtonModule,
        RippleModule,
        MenuModule,
        SidebarModule,
        MessageModule,
        CardModule,
        BadgeModule,
        ToastModule,
        ConfirmDialogModule,
        AvatarModule,
        DropdownModule,
        DragDropModule,
        HttpClientModule
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: HttpInterceptorService,
            multi: true
        },
        MessageService,
        ConfirmationService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
