import { Routes } from '@angular/router';
import { CreateArticleComponent } from './components/create-article/create-article.component';
import { PublishedArticlesComponent } from './components/published-articles/published-articles.component';
import { ReadArticleComponent } from './components/read-article/read-article.component';
import { EditArticleComponent } from './components/edit-article/edit-article.component';
import { ArticleEditGuard } from './guards/article-edit.guard';
import { AuthGuard } from '../auth/guards/auth.guard';

export const ARTICLE_ROUTES: Routes = [
    { path: '', component: PublishedArticlesComponent, pathMatch: 'full' },
    { path: 'create', component: CreateArticleComponent },
    { path: ':id', component: ReadArticleComponent, pathMatch: 'full' },
    { 
        path: 'edit/:id', 
        component: EditArticleComponent, 
        canActivate: [AuthGuard, ArticleEditGuard] 
    },
];