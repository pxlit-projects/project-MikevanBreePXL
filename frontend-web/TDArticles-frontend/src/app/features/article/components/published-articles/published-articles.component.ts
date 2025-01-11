import { Component, OnInit, ViewChild } from '@angular/core';
import { Article } from '../../../../shared/models/article.model';
import { ArticleItemComponent } from '../article-item/article-item.component';
import { ArticleService } from '../../services/article.service';
import { MatCardModule } from '@angular/material/card';
import { MatRipple, MatRippleModule } from '@angular/material/core';

import { RouterLink } from '@angular/router';
import { NotificationService } from '../../../notifications/services/notification.service';

@Component({
  selector: 'app-published-articles',
  standalone: true,
  imports: [
    MatCardModule,
    MatRippleModule,
    RouterLink,
    ArticleItemComponent
],
  templateUrl: './published-articles.component.html',
  styleUrl: './published-articles.component.css'
})
export class PublishedArticlesComponent implements OnInit {
  articles: Article[] = [];

  constructor(private articleService: ArticleService) {}

  ngOnInit() {
    this.articleService.fetchPublishedArticles()
      .subscribe(articles => this.articles = articles);
  }

  /** Reference to the directive instance of the ripple. */
  @ViewChild(MatRipple) ripple!: MatRipple;

  /** Shows a centered and persistent ripple. */
  launchRipple() {
    const rippleRef = this.ripple.launch({
      persistent: true
    });

    // Fade out the ripple later.
    rippleRef.fadeOut();
  }
}