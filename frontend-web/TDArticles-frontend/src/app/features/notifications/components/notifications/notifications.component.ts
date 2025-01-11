import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { ReviewNotification } from '../../../../shared/models/review-notification.model';

@Component({
  selector: 'app-notifications',
  template: `
    @for (notification of notifications; track notification) {
      <div>
        {{ notification.sender }}: {{ notification.message }}
      </div>
    }
    `
})
export class NotificationsComponent implements OnInit {

  notifications: ReviewNotification[] = [];

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.webSocketService.subscribeToUserNotifications().subscribe((notification) => {
      this.notifications.push(notification);
    });
  }
}