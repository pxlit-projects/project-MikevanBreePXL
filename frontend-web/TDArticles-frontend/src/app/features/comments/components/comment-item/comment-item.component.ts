import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ArticleComment } from '../../../../shared/models/article-comment.model';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, MatIconModule, FormsModule],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css'
})
export class CommentItemComponent {
  @Input() comment!: ArticleComment;
  @Output() deleteComment = new EventEmitter<void>();
  @Output() editComment = new EventEmitter<string>();

  isEditing = false;
  editedCommentText = '';

  constructor(public authService: AuthService) {}

  handleDeleteComment() {
    this.deleteComment.emit();
  }

  handleEditComment() {
    this.isEditing = true;
    this.editedCommentText = this.comment.comment;
  }

  saveEditComment() {
    this.editComment.emit(this.editedCommentText);
    this.isEditing = false;
  }

  cancelEditComment() {
    this.isEditing = false;
  }
}
