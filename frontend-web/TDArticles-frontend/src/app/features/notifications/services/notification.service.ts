import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { WebSocketService } from './web-socket.service';
import { filter } from 'rxjs';

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

  showNotification(notification: any): void {
    this.snackBar.open(
      `${notification.sender}: ${notification.message}`,
      'Close',
      {
        duration: 5000,
        horizontalPosition: 'end',
        verticalPosition: 'top',
        panelClass: ['notification-toast']
      }
    );
  }
}