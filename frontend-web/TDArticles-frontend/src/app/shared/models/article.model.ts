export class Article {
    id: number;
    title: string;
    author: string;
    description: string;
    published: boolean;

    constructor(id: number, title: string, author: string, description: string, published: boolean) {
      this.id = id;
      this.title = title;
      this.author = author;
      this.description = description;
      this.published = published;
    }
  }
  