import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { ArticleService } from '../services/article.service';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ArticleEditGuard {
  constructor(
    private articleService: ArticleService,
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    const articleId = Number(route.paramMap.get('id'));
    
    return this.articleService.fetchArticleById(articleId).pipe(
      map(article => {
        const isAuthor = article.author === this.authService.getCurrentUser()?.name;
        if (!isAuthor) {
          this.router.navigate(['/article/']);
        }
        return isAuthor;
      })
    );
  }
}