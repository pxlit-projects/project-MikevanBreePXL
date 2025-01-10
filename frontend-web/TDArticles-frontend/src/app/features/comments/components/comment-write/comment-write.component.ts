import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-comment-write',
  standalone: true,
  imports: [MatCardModule, MatInputModule, MatIconModule, FormsModule],
  templateUrl: './comment-write.component.html',
  styleUrl: './comment-write.component.css'
})
export class CommentWriteComponent {
  @Output() commentSubmitted = new EventEmitter<string>();
  commentText: string = '';

  onSubmit() {
    if (this.commentText.trim()) {
      this.commentSubmitted.emit(this.commentText);
      this.commentText = '';
    }
  }
}
