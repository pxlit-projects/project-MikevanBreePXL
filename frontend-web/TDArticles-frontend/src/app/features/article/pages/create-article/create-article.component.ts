import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ArticleCreationFormComponent } from '../../components/article-creation-form/article-creation-form.component';
import { ConceptArticlesListComponent } from '../../components/concept-articles-list/concept-articles-list.component';
import { Article } from '../../../../shared/models/article.model';
import { BehaviorSubject } from 'rxjs';
import { ArticleService } from '../../services/article.service';
import { SavedArticlesListComponent } from '../../components/saved-articles-list/saved-articles-list.component';

@Component({
  selector: 'app-create-article',
  standalone: true,
  imports: [ArticleCreationFormComponent, ConceptArticlesListComponent, SavedArticlesListComponent, CommonModule],
  templateUrl: './create-article.component.html',
  styleUrl: './create-article.component.css'
})
export class CreateArticleComponent implements OnDestroy {
  private selectedArticleSubject = new BehaviorSubject<Article | null>(null);
  selectedArticle$ = this.selectedArticleSubject.asObservable();

  constructor(
    private articleService: ArticleService,
    private router: Router
  ) {
    // Debug subscription
    this.selectedArticle$.subscribe(article => 
      console.log('Selected article changed:', article)
    );
  }

  createArticle(articleData: Article): void {
    this.articleService.submitArticle(articleData)
      .subscribe({
        next: () => this.router.navigate(['/article']),
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