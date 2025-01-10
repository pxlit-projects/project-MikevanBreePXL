import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ArticleComment } from '../../../shared/models/article-comment.model';
import { environment } from '@env/environment';
import { Observable, Subscription } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';

interface CommentRequest {
  article_id: number;
  author: string;
  comment: string;
}

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  public fetchComments(articleId: number): Observable<ArticleComment[]> {
    return this.http.get<ArticleComment[]>(`${environment.apiCommentUrl}article/${articleId}`);
  }

  sendComment(articleId: number, commentText: string): Subscription {
    const payload: CommentRequest = {
      article_id: articleId,
      author: this.authService.getCurrentUser()!.name ,
      comment: commentText
    };

    return this.http.post(
      `${environment.apiCommentUrl}`, 
      payload,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      }
    ).subscribe();
  }
}
