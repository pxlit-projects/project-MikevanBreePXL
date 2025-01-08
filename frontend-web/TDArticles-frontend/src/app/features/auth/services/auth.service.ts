import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../../../shared/models/user.model';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private router: Router) {}

    login(user: User): void {
        this.currentUserSubject.next(user);
        // localStorage.setItem('user', JSON.stringify(user));
        this.router.navigate(['/article/']);
    }

    logout(): void {
        this.currentUserSubject.next(null);
        // localStorage.removeItem('user');
        this.router.navigate(['/']);
    }

    getCurrentUser(): User | null {
        return this.currentUserSubject.value;
    }

    isLoggedIn(): boolean {
        return !!this.currentUserSubject.value;
    }
}