import { Component, OnInit } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleCreationFormComponent } from '../../components/article-creation-form/article-creation-form.component';
import { ArticleService } from '../../services/article.service';
import { Article } from '../../../../shared/models/article.model';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-edit-article',
  standalone: true,
  imports: [ArticleCreationFormComponent, AsyncPipe],
  templateUrl: './edit-article.component.html',
  styleUrl: './edit-article.component.css'
})
export class EditArticleComponent implements OnInit {
  private selectedArticleSubject = new BehaviorSubject<Article | null>(null);
  selectedArticle$ = this.selectedArticleSubject.asObservable();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private articleService: ArticleService
  ) {}

  ngOnInit(): void {
    const articleId = Number(this.route.snapshot.paramMap.get('id'));
    if (articleId) {
      this.articleService.fetchArticleById(articleId)
        .subscribe(article => this.selectedArticleSubject.next(article));
    }
  }

  editArticle(articleData: Article): void {
    this.articleService.submitArticle(articleData)
      .subscribe({
        next: () => this.router.navigate(['/article/']),
        error: (error) => console.error('Error updating article:', error)
      });
  }

  cancelEdit() {
    this.router.navigate(['/article/read', this.route.snapshot.paramMap.get('id')]);
  }
}
