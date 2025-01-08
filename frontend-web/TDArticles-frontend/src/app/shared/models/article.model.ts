export interface Article {
  id?: number;
  title: string;
  author: string;
  authorId?: number;
  content: string;
  published: boolean;
  concept?: boolean;
  createdAt?: Date;
}
