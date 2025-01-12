import { Component, EventEmitter, Output } from '@angular/core';
import { Article } from '../../../../shared/models/article.model';
import { ArticleService } from '../../services/article.service';
import { AuthService } from '../../../auth/services/auth.service';
import { SavedArticleCardComponent } from '../saved-article-card/saved-article-card.component';

@Component({
  selector: 'app-saved-articles-list',
  standalone: true,
  imports: [SavedArticleCardComponent],
  templateUrl: './saved-articles-list.component.html',
  styleUrl: './saved-articles-list.component.css'
})
export class SavedArticlesListComponent {
  articlesReadyToPublish: Article[] = [];
  @Output() articleSelected = new EventEmitter<Article>();

  constructor(private articleService: ArticleService, private authService: AuthService) { }

  ngOnInit(): void {
    this.fetchArticlesReadyToPublish();
  }

  private fetchArticlesReadyToPublish(): void {
    this.articleService.fetchArticlesReadyToPublish()
      .subscribe({
        next: (articles) => {
          this.articlesReadyToPublish = articles;
        },
        error: (error) => console.error('Error fetching concept articles:', error)
      });
  }
  
  selectArticle(article: Article): void {
    this.articleSelected.emit(article);
  }
}
