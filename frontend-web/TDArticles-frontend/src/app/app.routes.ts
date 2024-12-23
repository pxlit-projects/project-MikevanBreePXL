import { Routes } from '@angular/router';
import { ArticleListComponent } from './core/articles/article-list/article-list.component';
import { PublishedArticlesComponent } from './core/articles/published-articles/published-articles.component';

export const routes: Routes = [
    { path: '', component: PublishedArticlesComponent },
    { path: 'create', component: ArticleListComponent },
];
