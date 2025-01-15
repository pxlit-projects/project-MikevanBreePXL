import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ArticleComment } from '../../../shared/models/article-comment.model';
import { environment } from '@env/environment';
import { Observable, Subscription } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  public fetchComments(articleId: number): Observable<ArticleComment[]> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    return this.http.get<ArticleComment[]>(`${environment.apiCommentUrl}article/${articleId}`, { headers });
  }

  sendComment(articleId: number, commentText: string): Subscription {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    
    const body = {
      article_id: articleId,
      author: this.authService.getCurrentUser()!.name ,
      comment: commentText
    };

    return this.http.post(
      `${environment.apiCommentUrl}`, 
      body,
      { headers }
    ).subscribe();
  }

  editComment(commentId: number, comment: string): Observable<object> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    return this.http.put(`${environment.apiCommentUrl}${commentId}`, { comment }, { headers });
  }
  
  deleteComment(commentId: number): Observable<object> {
    let headers = new HttpHeaders();
    headers = headers.set('Username', this.authService.getCurrentUser()!.name);
    return this.http.delete(`${environment.apiCommentUrl}${commentId}`,{ headers });
  }
}
