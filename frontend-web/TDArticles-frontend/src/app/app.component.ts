import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from "./core/layout/navbar/navbar.component";
import { LoginComponent } from "./features/auth/components/login/login.component";
import { User } from './shared/models/user.model';
import { AuthService } from './features/auth/services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-TDArticles',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, LoginComponent, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  currentUser$: Observable<User | null>;

  constructor(
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit() {
    // Check stored login on init
    this.authService.loadUserFromStorage();
    this.cdr.detectChanges();
  }

  handleLogin(userData: User) {
    this.authService.login(userData);
  }
}
