export interface ArticleComment {
  id?: number;
  articleId: number;
  author: string;
  content: string;
  creationTime: Date;
}