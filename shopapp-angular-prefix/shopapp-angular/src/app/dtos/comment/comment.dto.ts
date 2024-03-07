export class CommentDto{
  product_id: number;
  user_id: number;
  fullname: string;
  comment_content: string;
  rating: number;
  order_id: number;
  created_at: Date;

  constructor(data: any) {
    this.product_id = data.product_id;
    this.user_id = data.user_id;
    this.fullname = data.fullname;
    this.comment_content = data.comment_content;
    this.rating = data.rating;
    this.order_id = data.order_id;
    this.created_at = data.created_at;
  }
}
