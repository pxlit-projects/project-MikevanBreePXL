import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '@env/environment';
import { ArticleCreationFormComponent } from '../article-creation-form/article-creation-form.component';
import { ConceptArticlesListComponent } from '../concept-articles-list/concept-articles-list.component';
import { Article } from '../../../shared/models/article.model';

@Component({
  selector: 'app-create-article',
  standalone: true,
  imports: [ArticleCreationFormComponent, ConceptArticlesListComponent],
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.css'],
  styles: [`
    .container { padding: 20px; }
  `]
})
export class CreateArticleComponent {
  constructor(
    private http: HttpClient,
    private router: Router
  ) {}
  
  selectedArticle: Article | null = null;

  createArticle(articleData: any): void {
    this.http.post(`${environment.apiPostUrl}create`, articleData)
      .subscribe({
        next: () => this.router.navigate(['/article/']),
        error: (error) => console.error('Error creating article:', error)
      });
  }

  onArticleSelected(article: Article): void {
    this.selectedArticle = article;
  }
}