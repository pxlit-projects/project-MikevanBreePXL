import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../features/auth/services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  constructor(public authService: AuthService) {}

  get isWriter(): boolean {
    return this.authService.getCurrentUser()?.role === 'writer';
  }

  get isReviewer(): boolean {
    return this.authService.getCurrentUser()?.role === 'reviewer';
  }

  logout(): void {
    this.authService.logout();
  }
}