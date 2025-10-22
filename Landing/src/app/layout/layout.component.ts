import { Component } from '@angular/core';
import { RouterModule } from "@angular/router";
import { HeaderComponent } from "./header/header.component";
import { FooterComponent } from "./footer/footer.component";
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  standalone: true,
  imports: [RouterModule, HeaderComponent, FooterComponent, ScrollToModule], // ✅ Import the module here
})
export class LayoutComponent { }
