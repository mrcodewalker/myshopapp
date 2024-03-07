import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {Product} from "../models/product";
import {Category} from "../models/category";
import {CreateCommentDto} from "../dtos/comment/create.comment.dto";

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiGetComments = `${environment.apiBaseUrl}/comments`;
  private apiCreateComment = `${environment.apiBaseUrl}/comments/create_comment/`;

  constructor(private http: HttpClient) {
  }
  getAllComments(id: number) : Observable<any>{
    return this.http.get(this.apiGetComments+`/get_comments_by_id/${id}`);
  }
  createComment(order_id: number, comment: CreateCommentDto) : Observable<any>{
    return this.http.post(this.apiCreateComment+order_id.toString(), comment);
  }
}
