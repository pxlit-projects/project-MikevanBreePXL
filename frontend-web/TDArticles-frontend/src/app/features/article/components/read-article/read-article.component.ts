import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Article } from '../../../../shared/models/article.model';
import { ArticleService } from '../../services/article.service';
import { ArticleItemComponent } from '../article-item/article-item.component';
import { CommentService } from '../../../comments/services/comment.service';
import { ArticleComment } from '../../../../shared/models/article-comment.model';
import { CommentItemComponent } from '../../../comments/components/comment-item/comment-item.component';
import { CommentWriteComponent } from '../../../comments/components/comment-write/comment-write.component';

@Component({
  selector: 'app-read-article',
  standalone: true,
  imports: [ArticleItemComponent, CommentItemComponent, CommentWriteComponent],
  templateUrl: './read-article.component.html',
  styleUrl: './read-article.component.css'
})
export class ReadArticleComponent implements OnInit {
  articleId: number = 0;
  article: Article | null = null;
  comments: ArticleComment[] = [];

  constructor(
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
        next: (article: Article) => this.article = article,
        error: (error) => console.error('Error getting article:', error)
      });
  }

  private fetchComments() {
    this.commentService.fetchComments(this.articleId)
    .subscribe({
      next: (comments: ArticleComment[]) => this.comments = comments,
      error: (error) => console.error('Error getting comments:', error)
    });
  }
  
  onCommentSubmitted($event: string) {
    // TODO: Add comment to the article
    console.log('Comment submitted:', $event);
  }
}
