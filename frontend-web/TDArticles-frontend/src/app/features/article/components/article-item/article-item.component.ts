import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Article } from '../../../../shared/models/article.model';
import { AuthService } from '../../../auth/services/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-article-item',
  standalone: true,
  imports: [
    RouterLink,
    MatCardModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './article-item.component.html',
  styleUrl: './article-item.component.css'
})
export class ArticleItemComponent {
  @Input() article!: Article;

  constructor(public authService: AuthService,
    private router: Router) {}

  isAuthor(): boolean {
    return this.authService.getCurrentUser()?.name === this.article.author;
  }

  onCardClick(event: MouseEvent): void {
    // Prevent navigation if clicking the edit button
    if (!(event.target as HTMLElement).closest('button')) {
      this.router.navigate(['/article/read', this.article.id]);
    }
  }
}
