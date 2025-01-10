import { Component, Input } from '@angular/core';
import { ArticleComment } from '../../../../shared/models/article-comment.model';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-comment-item',
  standalone: true,
  imports: [MatCardModule],
  templateUrl: './comment-item.component.html',
  styleUrl: './comment-item.component.css'
})
export class CommentItemComponent {
  @Input() comment!: ArticleComment;
}
