import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Article } from '../../../shared/models/article.model';

@Component({
  selector: 'app-article-item',
  standalone: true,
  imports: [
    MatCardModule
  ],
  templateUrl: './article-item.component.html',
  styleUrl: './article-item.component.css'
})
export class ArticleItemComponent {
  @Input() article!: Article;
}
