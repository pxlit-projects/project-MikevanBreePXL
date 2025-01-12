import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatRipple, MatRippleModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';

import { Article } from '../../../../shared/models/article.model';
import { ArticleItemComponent } from '../article-item/article-item.component';
import { ArticleFilters, ArticleService } from '../../services/article.service';

@Component({
  selector: 'app-published-articles',
  standalone: true,
  imports: [
    MatCardModule,
    MatRippleModule,
    RouterLink,
    ArticleItemComponent,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule
],
  templateUrl: './published-articles.component.html',
  styleUrl: './published-articles.component.css'
})
export class PublishedArticlesComponent implements OnInit, OnDestroy {
  articles: Article[] = [];
  private destroy$ = new Subject<void>();
  filterForm: FormGroup;

  constructor(private fb: FormBuilder, private articleService: ArticleService) {
    this.filterForm = this.fb.group({
      author: [''],
      content: [''],
      from: [null],
      to: [null],
      sortBy: ['date'],
      sortDirection: ['desc']
    });
  }

  ngOnInit() {
    // Form changes subscription
    this.filterForm.valueChanges.pipe(
      debounceTime(750),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => this.loadArticles());

    // Date picker specific subscriptions
    this.filterForm.get('fromDate')?.valueChanges.pipe(
      takeUntil(this.destroy$)
    ).subscribe(() => this.loadArticles());

    this.filterForm.get('toDate')?.valueChanges.pipe(
      takeUntil(this.destroy$)
    ).subscribe(() => this.loadArticles());

    this.loadArticles();
  }
  
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadArticles() {
    const filters: ArticleFilters = this.filterForm.value;
    this.articleService.fetchPublishedArticles(filters)
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