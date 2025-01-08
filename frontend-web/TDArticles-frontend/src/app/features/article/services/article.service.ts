import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Article } from '../../../shared/models/article.model';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  constructor(private http: HttpClient) {}
  
  fetchArticleById(articleId: number): Observable<Article> {
    return this.http.get<Article>(`${environment.apiArticleUrl}${articleId}`);
  }

  public fetchPublishedArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${environment.apiArticleUrl}`);
  }
  
  public fetchConceptArticles(authorName: string): Observable<Article[]> {
    return this.http.get<Article[]>(`${environment.apiArticleUrl}concepts/${authorName}`)
  }

  public createArticle(articleData: Article): Observable<Object> {
    return this.http.post(`${environment.apiArticleUrl}create`, articleData) as Observable<Object>;
  }
}