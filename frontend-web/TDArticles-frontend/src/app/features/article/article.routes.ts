import { Routes } from '@angular/router';
import { CreateArticleComponent } from './components/create-article/create-article.component';
import { PublishedArticlesComponent } from './components/published-articles/published-articles.component';
import { ReadArticleComponent } from './components/read-article/read-article.component';

export const ARTICLE_ROUTES: Routes = [
    { path: '', component: PublishedArticlesComponent, pathMatch: 'full' },
    { path: 'create', component: CreateArticleComponent },
    { path: ':id', component: ReadArticleComponent, pathMatch: 'full' }
];