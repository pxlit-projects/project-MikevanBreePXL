import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { ReviewService } from '../../services/review.service';
import { Article } from '../../../../shared/models/article.model';
import { ArticleService } from '../../../article/services/article.service';

@Component({
  selector: 'app-review-article',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule
],
  templateUrl: './review-article.component.html',
  styleUrl: './review-article.component.css', 
})
export class ReviewArticleComponent implements OnInit {
  article?: Article;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private articleService: ArticleService,
    private reviewService: ReviewService
  ) {}

  ngOnInit(): void {
    const articleId = Number(this.route.snapshot.paramMap.get('id'));
    this.articleService.fetchArticleById(articleId)
      .subscribe(article => this.article = article);
  }

  submitReview(reviewApproved: boolean): void {
    if (!this.article || !this.article.id) return;
    
    this.reviewService.submitReview(this.article.id, reviewApproved, this.article!.author)
      .subscribe(() => {
        this.router.navigate(['/review']);
      });
  }
}
