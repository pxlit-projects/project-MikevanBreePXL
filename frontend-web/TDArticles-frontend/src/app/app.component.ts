import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from "./core/layout/navbar/navbar.component";
import { LoginComponent } from "./features/auth/components/login/login.component";
import { User } from './shared/models/user.model';
import { AuthService } from './features/auth/services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-TDArticles',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, LoginComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  currentUser$: Observable<User | null>;

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  handleLogin(userData: User) {
    this.authService.login(userData);
  }

  logout() {
    this.authService.logout();
  }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
}
