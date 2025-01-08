import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Article } from '../../../shared/models/article.model';
import { environment } from '@env/environment';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { DatePipe, CommonModule } from '@angular/common';
import { AuthService } from '../../auth/services/auth.service';

@Component({
  selector: 'app-concept-articles-list',
  standalone: true,
  imports: [MatListModule, MatCardModule, DatePipe, CommonModule],
  templateUrl: './concept-articles-list.component.html',
  styleUrl: './concept-articles-list.component.css'
})
export class ConceptArticlesListComponent implements OnInit {
  conceptArticles: Article[] = [];
  @Output() articleSelected = new EventEmitter<Article>();

  constructor(private http: HttpClient, private authService: AuthService) { }

  ngOnInit(): void {
    this.fetchConceptArticles();
  }

  private fetchConceptArticles(): void {
    this.http.get<Article[]>(`${environment.apiPostUrl}concepts/${this.authService.getCurrentUser()!.name}`)
      .subscribe({
        next: (articles) => {
          this.conceptArticles = articles;
        },
        error: (error) => console.error('Error fetching concept articles:', error)
      });
  }
  
  selectArticle(article: Article): void {
    this.articleSelected.emit(article);
  }
}