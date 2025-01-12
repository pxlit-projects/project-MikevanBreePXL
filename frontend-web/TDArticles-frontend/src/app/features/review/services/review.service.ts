import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    return this.http.get<Article[]>(`${environment.apiReviewUrl}`, { headers });
  }

  getArticle(articleId: number): Observable<Article> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name)
    return this.http.get<Article>(`${environment.apiReviewUrl}${articleId}`, { headers});
  }

  submitReview(articleId: number, approved: boolean, receiver: string, rejectionNotes?: string): Observable<void> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name)
    return this.http.post<void>(`${environment.apiReviewUrl}${articleId}`, {
      approved,
      receiver,
      rejectionNotes
    }, { headers });
  }
}