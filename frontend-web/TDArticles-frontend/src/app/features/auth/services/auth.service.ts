import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../../../shared/models/user.model';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly USER_STORAGE_KEY = 'user';
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    public currentUser$ = this.currentUserSubject.asObservable();

    constructor(private router: Router) {}

    public loadUserFromStorage(): boolean {
        const storedUser = localStorage.getItem(this.USER_STORAGE_KEY);
        if (storedUser) {
            this.currentUserSubject.next(JSON.parse(storedUser));
            return true;
        }
        return false;
    }

    login(user: User): void {
        this.currentUserSubject.next(user);
        localStorage.setItem(this.USER_STORAGE_KEY, JSON.stringify(user));
        this.router.navigate(['/article/']);
    }

    logout(): void {
        this.currentUserSubject.next(null);
        localStorage.removeItem(this.USER_STORAGE_KEY);
        this.router.navigate(['/']);
    }

    getCurrentUser(): User | null {
        return this.currentUserSubject.value;
    }

    isLoggedIn(): boolean {
        return !!this.currentUserSubject.value;
    }
}