import { Component, Input } from '@angular/core';
import { Article } from '../../../shared/models/article.model';

@Component({
  selector: 'app-article-list',
  standalone: true,
  imports: [],
  templateUrl: './article-list.component.html',
  styleUrl: './article-list.component.css'
})
export class ArticleListComponent {
  @Input() articles: Article[] = [];
}
