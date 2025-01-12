import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Article } from '../../../../shared/models/article.model';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DatePipe, SlicePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { Review } from '../../../../shared/review.model';
import { ReviewService } from '../../../review/services/review.service';

@Component({
  selector: 'app-rejected-article-card',
  standalone: true,
  imports: [MatCardModule, MatIcon, MatButtonModule, MatProgressSpinnerModule, DatePipe, SlicePipe],
  templateUrl: './rejected-article-card.component.html',
  styleUrl: './rejected-article-card.component.css'
})
export class RejectedArticleCardComponent implements OnInit {
  @Input() article!: Article;
  @Output() articleSelected = new EventEmitter<Article>();
  review!: Review;
  deleteLoading = false;

  constructor(
    private router: Router,
    private articleService: ArticleService,
    private reviewService: ReviewService
  ) {}

  ngOnInit(): void {
    if (this.article?.id) {
      this.reviewService.getReview(this.article.id).subscribe({
        next: (review) => this.review = review,
        error: (error) => console.error('Error fetching review:', error)
      });
    }
  }

  selectArticle(article: Article): void {
    this.articleSelected.emit(article);
  }
  
  deleteArticle(articleId: number): void {
    this.deleteLoading = true;
    console.log("Pressed!");
    this.articleService.deleteArticle(articleId).subscribe();

    this.router.navigate(['/article']);
  }
}
