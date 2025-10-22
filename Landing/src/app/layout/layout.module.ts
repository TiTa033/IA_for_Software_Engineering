import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

// Scroll To
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';
import { ScrollspyDirective } from '../scrollspy.directive';

// Bootstrap Component
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';

// Standalone Components
import { HeaderComponent } from "./header/header.component";
import { FooterComponent } from "./footer/footer.component";
import {LayoutComponent} from "./layout.component";

@NgModule({
  declarations: [
    LayoutComponent,
    ScrollspyDirective
  ],
  imports: [
    CommonModule,
    RouterModule,
    ScrollToModule.forRoot(),
    BsDropdownModule.forRoot(),
    HeaderComponent, // ✅ Standalone components must be imported!
    FooterComponent  // ✅ Standalone components must be imported!
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA] // ✅ Keep this if using Web Components
})
export class LayoutModule { }
