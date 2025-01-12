import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Article } from '../../../../shared/models/article.model';
import { ArticleService } from '../../services/article.service';
import { AuthService } from '../../../auth/services/auth.service';
import { SavedArticleCardComponent } from '../saved-article-card/saved-article-card.component';
import { RejectedArticleCardComponent } from '../rejected-article-card/rejected-article-card.component';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-saved-articles-list',
  standalone: true,
  imports: [
    CommonModule,
    SavedArticleCardComponent, 
    RejectedArticleCardComponent
  ],
  templateUrl: './saved-articles-list.component.html',
  styleUrl: './saved-articles-list.component.css'
})
export class SavedArticlesListComponent implements OnInit {
  articlesRejected: Article[] = [];
  articlesReadyToPublish: Article[] = [];
  @Output() articleSelected = new EventEmitter<Article>();

  constructor(
    private articleService: ArticleService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.fetchArticlesReadyToPublish();
    this.fetchFailedArticles();
  }

  private fetchArticlesReadyToPublish(): void {
    this.articleService.fetchArticlesReadyToPublish()
      .subscribe({
        next: (articles) => {
          this.articlesReadyToPublish = articles;
        },
        error: (error) => console.error('Error fetching ready articles:', error)
      });
  }

  private fetchFailedArticles(): void {
    this.articleService.fetchRejectedArticles()
      .subscribe({
        next: (articles) => {
          this.articlesRejected = articles;
        },
        error: (error) => console.error('Error fetching rejected articles:', error)
      });
  }
  
  selectArticle(article: Article): void {
    this.articleSelected.emit(article);
  }

  
editArticle($event: Article) {
    this.router.navigate(['/article/edit', $event.id]);
  }
}