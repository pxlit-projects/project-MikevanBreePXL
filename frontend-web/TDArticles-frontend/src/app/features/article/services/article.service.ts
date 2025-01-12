import { Injectable } from '@angular/core';
import { formatDate } from '@angular/common';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Article } from '../../../shared/models/article.model';
import { environment } from '@env/environment';
import { AuthService } from '../../auth/services/auth.service';

export interface ArticleFilters {
  id?: number;
  author?: string;
  content?: string;
  from?: Date;
  to?: Date;
  sortBy?: 'date' | 'author' | 'title';
  sortDirection?: 'asc' | 'desc';
}

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

  public fetchPublishedArticles(filters?: ArticleFilters): Observable<Article[]> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);

    let params = new HttpParams();
    if (filters?.author) params = params.set('author', filters.author);
    if (filters?.content) params = params.set('content', filters.content);
    if (filters?.from) {
      const fromDate = formatDate(filters.from, 'yyyy-MM-dd\'T\'HH:mm:ss', 'en');
      params = params.set('from', fromDate);
    }
    if (filters?.to) {
      const toDate = formatDate(filters.to, 'yyyy-MM-dd\'T\'HH:mm:ss', 'en');
      params = params.set('to', toDate);
    }

    return this.http.get<Article[]>(`${environment.apiArticleUrl}`, { headers, params }).pipe(
      map(articles => {
        if (filters?.sortBy === 'title') {
          articles = articles.sort((a, b) => a.title.localeCompare(b.title));
        } else if (filters?.sortBy === 'date') {
          articles = articles.sort((a, b) => {
            const dateA = new Date(a.creationTime!).getTime();
            const dateB = new Date(b.creationTime!).getTime();
            return dateA - dateB;
          });
        } else if (filters?.sortBy === 'author') {
          articles = articles.sort((a, b) => a.author.localeCompare(b.author));
        }

        if (filters?.sortDirection === 'desc') {
          articles = articles.reverse();
        }
        return articles;
      })
    );
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