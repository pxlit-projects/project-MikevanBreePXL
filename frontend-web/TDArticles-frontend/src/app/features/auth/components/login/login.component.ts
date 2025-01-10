import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatButtonModule } from '@angular/material/button';
import { User } from '../../../../shared/models/user.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  @Output() loginSuccess = new EventEmitter<User>();
  
  name = '';
  role: 'writer' | 'reviewer' | 'public' | '' = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Check for saved login
    if (this.authService.loadUserFromStorage()) {
      this.authService.login(this.authService.getCurrentUser()!);
    }
  }

  onSubmit() {
    if (this.name && this.role) {
      this.loginSuccess.emit({ name: this.name, role: this.role });
    }
  }
}