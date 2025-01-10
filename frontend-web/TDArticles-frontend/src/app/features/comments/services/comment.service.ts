import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ArticleComment } from '../../../shared/models/article-comment.model';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) {}

  public fetchComments(articleId: number): Observable<ArticleComment[]> {
    return this.http.get<ArticleComment[]>(`${environment.apiCommentUrl}article/${articleId}`);
  }
}
