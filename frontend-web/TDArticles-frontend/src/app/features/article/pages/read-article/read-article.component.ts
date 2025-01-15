import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Article } from '../../../../shared/models/article.model';
import { ArticleService } from '../../services/article.service';
import { CommentService } from '../../../comments/services/comment.service';
import { ArticleComment } from '../../../../shared/models/article-comment.model';
import { CommentItemComponent } from '../../../comments/components/comment-item/comment-item.component';
import { CommentWriteComponent } from '../../../comments/components/comment-write/comment-write.component';
import { ArticleItemComponent } from '../../components/article-item/article-item.component';
import { BehaviorSubject } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-read-article',
  standalone: true,
  imports: [ArticleItemComponent, CommentItemComponent, CommentWriteComponent, AsyncPipe],
  templateUrl: './read-article.component.html',
  styleUrl: './read-article.component.css'
})
export class ReadArticleComponent implements OnInit {
  articleId = 0;
  private articleSubject = new BehaviorSubject<Article | null>(null);
  article$ = this.articleSubject.asObservable();
  private commentsSubject = new BehaviorSubject<ArticleComment[]>([]);
  comments$ = this.commentsSubject.asObservable();

  constructor(
    private authService: AuthService,
    private articleService: ArticleService,
    private commentService: CommentService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.articleId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchArticle();
    this.fetchComments();
  }

  private fetchArticle() {
    this.articleService.fetchArticleById(this.articleId)
      .subscribe({
        next: (article: Article) => this.articleSubject.next(article),
        error: (error) => {
          console.error('Error getting article:', error);
          this.articleSubject.next(null);
        }
      });
  }

  private fetchComments() {
    this.commentService.fetchComments(this.articleId)
      .subscribe({
        next: (comments: ArticleComment[]) => this.commentsSubject.next(comments),
        error: (error) => console.error('Error getting comments:', error)
      });
  }
  
  onCommentSubmitted($event: string) {
    this.commentService.sendComment(this.articleId, $event)
      .add(() => this.fetchComments());
  }

  onEditComment(commentId: number, newCommentText: string) {
    this.commentService.editComment(commentId, newCommentText)
      .subscribe(() => this.fetchComments());
  }
  
  onDeleteComment(commentId: number) {
    this.commentService.deleteComment(commentId).subscribe()
      .add(() => this.fetchComments());
  }
}
