import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ReviewService } from '../../services/review.service';
import { Article } from '../../../../shared/models/article.model';

@Component({
  selector: 'app-review-overview',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, RouterLink],
  templateUrl: './review-overview.component.html',
  styleUrls: ['./review-overview.component.css']
})
export class ReviewOverviewComponent implements OnInit {
  articles: Article[] = [];
  displayedColumns: string[] = ['title', 'author', 'submissionDate', 'actions'];

  constructor(private reviewService: ReviewService) {}

  ngOnInit(): void {
    this.loadPendingArticles();
  }

  private loadPendingArticles(): void {
    this.reviewService.getPendingArticles()
      .subscribe(articles => this.articles = articles);
  }
}