import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../../../shared/models/article.model';
import { environment } from '@env/environment';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  constructor(private http: HttpClient,
    private authService: AuthService
  ) { }

  getPendingArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${environment.apiReviewUrl}`);
  }

  getArticle(articleId: number): Observable<Article> {
    return this.http.get<Article>(`${environment.apiReviewUrl}${articleId}`);
  }

  submitReview(articleId: number, approved: boolean, receiver: string): Observable<void> {
    return this.http.post<void>(`${environment.apiReviewUrl}${articleId}`, {
      approved,
      reviewer: this.authService.getCurrentUser()?.name,
      receiver
    });
  }
}