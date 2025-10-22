import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { KeycloakService } from '../../keycloak/keycloak.service';

export const HttpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService); // Inject KeycloakService dynamically
  const token = keycloakService.keycloak.token;

  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(authReq);
  }

  return next(req);
};
