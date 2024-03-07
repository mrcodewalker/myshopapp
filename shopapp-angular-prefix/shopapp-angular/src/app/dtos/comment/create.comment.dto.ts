export class CreateCommentDto{
  product_id: number;
  user_id: number;
  comment_content: string;
  rating: number;

  constructor(data: any) {
    this.product_id = data.product_id;
    this.user_id = data.user_id;
    this.comment_content = data.comment_content;
    this.rating = data.rating;
  }
}
