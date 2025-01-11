import { Component, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';

@Component({
  selector: 'app-notifications',
  template: `
    <div *ngFor="let notification of notifications">
      {{ notification.sender }}: {{ notification.message }}
    </div>
  `
})
export class NotificationsComponent implements OnInit {

  notifications: any[] = [];

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.webSocketService.subscribeToUserNotifications().subscribe((notification) => {
      this.notifications.push(notification);
    });
  }
}