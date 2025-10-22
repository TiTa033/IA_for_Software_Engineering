// Angular import
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

// Project import
import { SharedModule } from 'src/app/back-office/demo/shared/shared.module';

@Component({
  selector: 'app-sample-page',
  standalone: true, // ✅ Make it a standalone component
  imports: [CommonModule, SharedModule], // ✅ Import required modules
  templateUrl: './sample-page.component.html',
  styleUrls: ['./sample-page.component.scss']
})
export default class SamplePageComponent {}
