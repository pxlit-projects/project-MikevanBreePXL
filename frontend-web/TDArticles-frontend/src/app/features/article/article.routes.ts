import { Routes } from '@angular/router';
import { CreateArticleComponent } from './create-article/create-article.component';
import { PublishedArticlesComponent } from './published-articles/published-articles.component';

export const ARTICLE_ROUTES: Routes = [
    { path: '', component: PublishedArticlesComponent, pathMatch: 'full' },
    { path: 'create', component: CreateArticleComponent },
];