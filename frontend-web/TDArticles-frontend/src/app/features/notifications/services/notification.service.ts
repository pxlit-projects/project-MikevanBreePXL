import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { WebSocketService } from './web-socket.service';
import { filter } from 'rxjs';
import { ReviewNotification } from '../../../shared/models/review-notification.model';
import { NotificationSnackbarPopupComponent } from '../components/notification-snackbar-popup/notification-snackbar-popup.component';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  constructor(
    private webSocketService: WebSocketService,
    private snackBar: MatSnackBar
  ) {}

  connectWebSocket(): void {
    this.webSocketService.connected$.pipe(
      filter(connected => connected)
    ).subscribe(() => {
      this.webSocketService.subscribeToUserNotifications()
        .subscribe(notification => {
          this.showNotification(notification);
        });
    });
  }

  showNotification(notification: ReviewNotification): void {
    const message = notification.message.replace(/\n/g, '<br>');

    this.snackBar.openFromComponent(NotificationSnackbarPopupComponent, {
      data: {
        sender: notification.sender,
        message: message
      },
      duration: 5000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['notification-toast']
    });
  }
}