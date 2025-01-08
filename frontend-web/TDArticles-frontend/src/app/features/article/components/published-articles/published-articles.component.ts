import { Component, OnInit } from '@angular/core';
import { Article } from '../../../../shared/models/article.model';
import { ArticleItemComponent } from '../article-item/article-item.component';
import { ArticleService } from '../../services/article.service';

@Component({
  selector: 'app-published-articles',
  standalone: true,
  imports: [ArticleItemComponent],
  templateUrl: './published-articles.component.html',
  styleUrl: './published-articles.component.css'
})
export class PublishedArticlesComponent implements OnInit {
  articles: Article[] = [];

  constructor(private articleService: ArticleService) {}

  ngOnInit() {
    this.articleService.fetchPublishedArticles().subscribe(articles => this.articles = articles);
  }
}