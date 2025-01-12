import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { Article } from '../../../../shared/models/article.model';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { DatePipe, SlicePipe, AsyncPipe } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ArticleService } from '../../services/article.service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-concept-articles-list',
  standalone: true,
  imports: [
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    DatePipe,
    SlicePipe,
    AsyncPipe
  ],
  templateUrl: './concept-articles-list.component.html',
  styleUrl: './concept-articles-list.component.css'
})
export class ConceptArticlesListComponent implements OnInit, OnDestroy {
  @Output() articleSelected = new EventEmitter<Article>();
  private articlesSubject = new BehaviorSubject<Article[]>([]);
  articles$ = this.articlesSubject.asObservable();
  deleteLoading = false;

  constructor(
    private router: Router,
    private articleService: ArticleService
  ) {}

  ngOnInit(): void {
    this.fetchConceptArticles();
  }

  private fetchConceptArticles(): void {
    this.articleService.fetchConceptArticles()
      .subscribe({
        next: (articles) => this.articlesSubject.next(articles),
        error: (error) => console.error('Error fetching concept articles:', error)
      });
  }

  ngOnDestroy(): void {
    this.articlesSubject.complete();
  }

  selectArticle(article: Article): void {
    this.articleSelected.emit(article);
  }
  
  deleteArticle(articleId: number): void {
    this.deleteLoading = true;
    console.log("Pressed!");
    this.articleService.deleteArticle(articleId).subscribe();

    // execute after 1 second
    setTimeout(() => {
      this.deleteLoading = false;
      this.fetchConceptArticles();
      console.log("Redirecting!");
    }, 500);
  }
}