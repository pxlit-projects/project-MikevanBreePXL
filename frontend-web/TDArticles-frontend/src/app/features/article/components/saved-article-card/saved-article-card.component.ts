import { DatePipe, SlicePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Article } from '../../../../shared/models/article.model';
import { MatIcon } from '@angular/material/icon';
import { ArticleService } from '../../services/article.service';

@Component({
  selector: 'app-saved-article-card',
  standalone: true,
  imports: [MatCardModule, MatIcon, DatePipe, SlicePipe],
  templateUrl: './saved-article-card.component.html',
  styleUrl: './saved-article-card.component.css'
})
export class SavedArticleCardComponent {
@Input() article!: Article;
@Input() rejectionNotes?: string;

  constructor(private articleService: ArticleService) { }
  
  submitPublish() {
    this.articleService.publishArticle(this.article.id!, true)
      .subscribe(() => {
        console.log('Article published');
      });
  }
}
