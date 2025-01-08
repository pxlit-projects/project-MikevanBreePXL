import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '@env/environment';
import { ArticleCreationFormComponent } from '../article-creation-form/article-creation-form.component';
import { ConceptArticlesListComponent } from '../concept-articles-list/concept-articles-list.component';
import { Article } from '../../../shared/models/article.model';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-create-article',
  standalone: true,
  imports: [ArticleCreationFormComponent, ConceptArticlesListComponent, CommonModule],
  templateUrl: './create-article.component.html'
})
export class CreateArticleComponent implements OnDestroy {
  private selectedArticleSubject = new BehaviorSubject<Article | null>(null);
  selectedArticle$ = this.selectedArticleSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Debug subscription
    this.selectedArticle$.subscribe(article => 
      console.log('Selected article changed:', article)
    );
  }

  createArticle(articleData: any): void {
    this.http.post(`${environment.apiPostUrl}create`, articleData)
      .subscribe({
        next: () => this.router.navigate(['/articles/']),
        error: (error) => console.error('Error creating article:', error)
      });
  }

  onArticleSelected(article: Article): void {
    console.log('Article selected:', article);
    this.selectedArticleSubject.next(article);
  }

  cancelConceptEdit(): void {
    console.log('Cancelling concept edit');
    this.selectedArticleSubject.next(null);
  }

  ngOnDestroy(): void {
    this.selectedArticleSubject.complete();
  }
}