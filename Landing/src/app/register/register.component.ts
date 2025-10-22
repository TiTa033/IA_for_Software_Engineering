import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  year =(new Date().getFullYear());

}
