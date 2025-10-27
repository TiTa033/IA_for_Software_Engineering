import {APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import {HttpClientModule, provideHttpClient, withInterceptors} from '@angular/common/http';
import { LayoutModule } from "@angular/cdk/layout";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { KeycloakService } from "./back-office/@theme/keycloak/keycloak.service";
import { LoginComponent } from "./login/login.component";
import { ResetpasswordComponent } from "./resetpassword/resetpassword.component";
import { RegisterComponent } from './register/register.component';
import { HttpTokenInterceptor } from "./back-office/@theme/services/interceptor/http-token.interceptor";
import { RouterModule } from '@angular/router';
import { HeaderComponent } from "./layout/header/header.component";
import { FooterComponent } from "./layout/footer/footer.component";
import { CommonModule } from '@angular/common';
import { ScrollToModule } from '@nicky-lenaers/ngx-scroll-to';
import { MaterialPredictorComponent } from './material-predictor/material-predictor.component';
import { ChatComponent } from './chat/chat.component';



//export function kcFactory(kcService: KeycloakService) {
  //return () => kcService.init();
//}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ResetpasswordComponent,
    MaterialPredictorComponent,  // 👈 ajoute-le ici
    ChatComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    LayoutModule,
    BrowserAnimationsModule,
    FormsModule,
    RouterModule,
    RegisterComponent, // ✅ Standalone component (imported, not declared)
    HeaderComponent,   // ✅ Ensure these components exist
    FooterComponent,
    ScrollToModule.forRoot(), // Ensure it's registered properly
    HttpClientModule  // <-- Add HttpClientModule to the imports array



  ],
  providers: [
   // provideHttpClient(withInterceptors([HttpTokenInterceptor])), // ✅ Fixed functional interceptor
    //{
     // provide: APP_INITIALIZER,
     // deps: [KeycloakService],
     // useFactory: kcFactory,
      //multi: true
    //}
  ],
  //schemas: [CUSTOM_ELEMENTS_SCHEMA], // ✅ Add this if needed
  bootstrap: [AppComponent]
})
export class AppModule { }
