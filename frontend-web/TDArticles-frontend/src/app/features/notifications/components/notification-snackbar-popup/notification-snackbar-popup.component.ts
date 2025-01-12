import { Component, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { NotificationData } from '../../models/notification-data.interface';

@Component({
  selector: 'app-notification-snackbar-popup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification-snackbar-popup.component.html',
  styleUrl: './notification-snackbar-popup.component.css'
})
export class NotificationSnackbarPopupComponent {
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: NotificationData) {}
}
