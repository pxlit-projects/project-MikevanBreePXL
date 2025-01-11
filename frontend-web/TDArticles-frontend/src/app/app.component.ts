import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from "./core/layout/navbar/navbar.component";
import { LoginComponent } from "./features/auth/components/login/login.component";
import { User } from './shared/models/user.model';
import { AuthService } from './features/auth/services/auth.service';
import { Observable } from 'rxjs';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NotificationService } from './features/notifications/services/notification.service';

@Component({
  selector: 'app-tdarticles',
  standalone: true,
  imports: [
    RouterOutlet,
    NavbarComponent,
    LoginComponent,
    CommonModule,
    MatSnackBarModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  currentUser$: Observable<User | null>;

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService,
    private cdr: ChangeDetectorRef
  ) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    // Check stored login on init
    if (this.authService.loadUserFromStorage()) {
        // Connect Websocket for notifications
        this.notificationService.connectWebSocket();
    }
    this.cdr.detectChanges();
  }

  handleLogin(userData: User): void {
    this.authService.login(userData);
    // Connect Websocket for notifications
    this.notificationService.connectWebSocket();
  }
}
