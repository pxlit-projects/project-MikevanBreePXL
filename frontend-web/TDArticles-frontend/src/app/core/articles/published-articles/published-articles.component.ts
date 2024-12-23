import { Component, Input } from '@angular/core';
import { Article } from '../../../shared/models/article.model';
import { ArticleItemComponent } from '../article-item/article-item.component';

@Component({
  selector: 'app-published-articles',
  standalone: true,
  imports: [
    ArticleItemComponent
  ],
  templateUrl: './published-articles.component.html',
  styleUrl: './published-articles.component.css'
})
export class PublishedArticlesComponent {
  articles: Article[] = [
    {
      id: 1,
      title: 'Title text Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ',
      description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
      author: 'Author 1',
      published: true
    },
    {
      id: 2,
      title: 'Article 2',
      description: 'Description 2',
      author: 'Author 2',
      published: true
    },
    {
      id: 3,
      title: 'Article 3',
      description: 'Description 3',
      author: 'Author 3',
      published: false
    },
    {
      id: 4,
      title: 'Article 4',
      description: 'Description 4',
      author: 'Author 4',
      published: true
    }
  ];
}
