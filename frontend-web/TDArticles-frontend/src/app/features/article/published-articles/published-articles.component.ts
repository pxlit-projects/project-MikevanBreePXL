import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Article } from '../../../shared/models/article.model';
import { environment } from '@env/environment';
import { ArticleItemComponent } from '../article-item/article-item.component';

@Component({
  selector: 'app-published-articles',
  standalone: true,
  imports: [HttpClientModule, ArticleItemComponent],
  templateUrl: './published-articles.component.html',
  styleUrl: './published-articles.component.css'
})
export class PublishedArticlesComponent implements OnInit {
  articles: Article[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchPublishedArticles();
  }

  private fetchPublishedArticles() {
    this.http.get<Article[]>(`${environment.apiPostUrl}`)
      .subscribe({
        next: (articles) => {
          this.articles = articles;
        },
        error: (error) => {
          console.error('Error fetching published articles:', error);
        }
      });
  }
}