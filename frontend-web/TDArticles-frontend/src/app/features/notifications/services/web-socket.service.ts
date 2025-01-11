import { Injectable, OnDestroy } from '@angular/core';
import { environment } from '@env/environment';
import { Observable, EMPTY, BehaviorSubject, filter, take } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';
import { Client, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService implements OnDestroy {
  private stompClient!: Client;
  private connectionSubject = new BehaviorSubject<boolean>(false);
  public connected$ = this.connectionSubject.asObservable();

  constructor(private authService: AuthService) {
    this.initializeWebSocket();
  }

  private initializeWebSocket(): void {
    const socket = new SockJS('http://localhost:8089/notification-ws/ws');
    this.stompClient = Stomp.over(socket);

    this.stompClient.onConnect = () => {
      console.log('Connected to WebSocket');
      this.connectionSubject.next(true);
    };

    this.stompClient.onWebSocketError = (error) => {
      console.error('WebSocket Error:', error);
      this.connectionSubject.next(false);
    };

    this.stompClient.activate();
  }

  public subscribeToUserNotifications(): Observable<any> {
    return new Observable(subscriber => {
      this.connected$.pipe(
        filter(connected => connected),
        take(1)
      ).subscribe(() => {
        this.stompClient.subscribe(`/user/${this.authService.getCurrentUser()!.name}/notifications`, message => {
          console.log('Received message:', message);
          subscriber.next(JSON.parse(message.body));
        });
      });
    });
  }

  ngOnDestroy() {
    this.connectionSubject.complete();
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}