import { Component } from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {MenuModule} from "primeng/menu";
import {MenuItem} from "primeng/api";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-header-bar',
  standalone: true,
  imports: [
    AvatarModule,
    ButtonModule,
    RippleModule,
    MenuModule,
    ToastModule
  ],
  templateUrl: './header-bar.component.html',
  styleUrl: './header-bar.component.scss'
})
export class HeaderBarComponent {
  items: Array<MenuItem> = [
    {
      label: 'Profile',
      icon: 'pi pi-user'
    },
    {
      label: 'Settings',
      icon: 'pi pi-cog'
    },
    {
      label: 'Sign out',
      icon: 'pi pi-sing-out'
    },
  ];

}
