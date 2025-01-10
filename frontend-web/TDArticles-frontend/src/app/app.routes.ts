import { Routes } from '@angular/router';
import { AuthGuard } from './features/auth/guards/auth.guard';

export const routes: Routes = [
    {
        path: 'auth',
        loadChildren: () => import('./features/auth/auth.routes').then(m => m.AUTH_ROUTES)
    },
    {
        path: 'article',
        loadChildren: () => import('./features/article/article.routes').then(m => m.ARTICLE_ROUTES),
        canActivate: [AuthGuard]
    },
    {
        path: 'review',
        loadChildren: () => import('./features/review/review.routes').then(m => m.REVIEW_ROUTES),
        canActivate: [AuthGuard]
    },
    { path: '', redirectTo: '/article/', pathMatch: 'full' }
];