export interface Article {
  id?: number;
  title: string;
  author: string;
  concept?: boolean;
  content: string;
  creationTime?: Date;
}
