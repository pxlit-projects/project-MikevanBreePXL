import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ArticleComment } from '../../../../shared/models/article-comment.model';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css'
})
export class CommentItemComponent {
  @Input() comment!: ArticleComment;
  @Output() deleteComment = new EventEmitter<void>();

  constructor(public authService: AuthService) {}
  
  handleDeleteComment() {
    this.deleteComment.emit();
  }
}
