import { Routes } from '@angular/router';
import { PublishedArticlesComponent } from './core/articles/published-articles/published-articles.component';
import { CreateArticleComponent } from './core/articles/create-article/create-article.component';

export const routes: Routes = [
    { path: '', component: PublishedArticlesComponent, pathMatch: 'full' },
    { path: 'articles/create', component: CreateArticleComponent },
];
