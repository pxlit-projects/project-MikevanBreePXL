export interface ArticleComment {
  id?: number;
  articleId: number;
  author: string;
  comment: string;
  creationTime: Date;
}