import { Routes } from '@angular/router';
import { ReviewOverviewComponent } from './components/review-overview/review-overview.component';
import { ReviewArticleComponent } from './components/review-article/review-article.component';

export const REVIEW_ROUTES: Routes = [
    { path: '', component: ReviewOverviewComponent, pathMatch: 'full' },
    { path: ':id', component: ReviewArticleComponent }
];