import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../../../shared/models/article.model';
import { environment } from '@env/environment';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}
  
  fetchArticleById(articleId: number): Observable<Article> {
      let headers = new HttpHeaders();
      headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    return this.http.get<Article>(`${environment.apiArticleUrl}${articleId}`, { headers });
  }

  public fetchPublishedArticles(): Observable<Article[]> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    return this.http.get<Article[]>(`${environment.apiArticleUrl}`, { headers });
  }
  
  public fetchConceptArticles(authorName: string): Observable<Article[]> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    return this.http.get<Article[]>(`${environment.apiArticleUrl}concepts/${authorName}`, { headers })
  }

  public submitArticle(articleData: Article): Observable<object> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    if (articleData.id) {
      return this.http.put(`${environment.apiArticleUrl}${articleData.id}`, articleData, { headers });
    }
    return this.http.post(`${environment.apiArticleUrl}create`, articleData, { headers })
  }
}