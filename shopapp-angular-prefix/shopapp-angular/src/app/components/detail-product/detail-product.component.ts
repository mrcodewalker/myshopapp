import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/product";
import {ProductService} from "../../services/product.service";
import {environment} from "../../environments/environment";
import {ProductImage} from "../../models/product.image";
import {CartService} from "../../services/cart.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthGuard} from "../../guards/auth.guard";
import {CommentService} from "../../services/comment.service";
import {CommentDto} from "../../dtos/comment/comment.dto";
import {OrderResponse} from "../../responses/order/order.response";
import {OrderService} from "../../services/order.service";
import {CreateCommentDto} from "../../dtos/comment/create.comment.dto";
import {UserService} from "../../services/user.service";
import {ProductSoldDto} from "../../dtos/order/product.sold.dto";

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss']
})
export class DetailProductComponent implements OnInit{
    product?: Product;
    productId: number = 0;
    comments: CommentDto[] = [];
    afterSortComments: CommentDto[] = [];
    productSold: ProductSoldDto = {
      message: "",
      quantity: 0
    };
    message: string ="";
    quantity: number = 1;
    userId: number = 1;
    createComment: CreateCommentDto = {
      product_id: this.productId,
      comment_content: "",
      rating: 5,
      user_id: this.userId
    };
    idDirect: number = 1;
    orders: OrderResponse[] = [];
    formComment : boolean = false;
    currentImageIndex: number = 0;
    constructor(
      private productService: ProductService,
      private cartService: CartService,
      private router: ActivatedRoute,
      private route: Router,
      private commentService: CommentService,
      private orderService: OrderService,
      private userService: UserService
      // private categoryService: CategoryService,
      // private router: Router,
      // private activatedRoute: ActivatedRoute
    ) {
    }
    ngOnInit(): void {
      this.userId = Number(this.userService.getUserResponseFromLocalStorage()?.id);
      this.router.params.subscribe(params => {
        this.idDirect = params['id'];
        debugger;
      });
      const idParam = this.idDirect;
      debugger
      if (idParam !== null){
        this.productId = +idParam;
      }
      this.createComment.user_id = this.userId;
      this.createComment.product_id = this.productId;
      this.orderService.getOrdersByProductId(this.productId).subscribe({
        next: (response: any) =>{
          debugger;
          this.productSold.message = response.message;
          this.productSold.quantity = response.quantity;
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger;
          console.log("Error fetching data error: "+error.error.message);
        }
      });
      this.commentService.getAllComments(this.productId).subscribe({
        next: (response_comment: any) => {
          debugger;
          response_comment.comments.forEach((items: CommentDto) => {
              const clone = new Date(items.created_at);
              clone.setHours(clone.getHours()-7);
              items.created_at = clone;
            debugger;
          });
          this.comments = response_comment.comments;
          this.message = response_comment.message;
          const storedIdsJSON = localStorage.getItem("storedIds");
          this.sortComments();
          localStorage.setItem('productIdComment',this.productId.toString());
          debugger;
        },
        complete: () => {
          debugger;
        },
        error: (error: any) =>{
          debugger;
          console.log("Error fetching data: error "+error.error.message);
        }
      })
      localStorage.removeItem('storedIds');
      const formCommentJSON = localStorage.getItem('formComment');
      this.orderService.getAllOrdersByUserId().subscribe({
        next: (response: any) =>{
          debugger;
          this.orders = response.orders;
          let id: number[] = [];
          this.orders.forEach((items) => {
            if (items.status.toLowerCase() == 'delivered'&& items.active) {
              items.order_details.forEach((item) => {
                if (item.product.id === this.productId){
                  id.push(items.id);
                }
              })
            }
            debugger;
            let count = 0;
            if (id.length>0){
              this.comments.forEach((comment) =>{
                if (!id.includes(comment.order_id)){
                  count++;
                } else {
                  this.formComment = false;
                  return;
                }
              });
            }
            if (count===this.comments.length && id.length > 0){
              this.formComment = true;
            } else {
              this.formComment = false;
            }
          });
          const idJSON = JSON.stringify(id);
          localStorage.setItem("storedIds", idJSON);
          if (id==null){
            this.formComment = false;
          }
          debugger;
        },
        complete: () =>{
          debugger;
        },
        error: (error: any) =>{
          debugger;
          console.log("Error fetching data: error ",error.error.message);
        }
      })
      const storedFormComment = localStorage.getItem('formComment');
      if (storedFormComment !== null) {
        this.formComment = JSON.parse(storedFormComment);
      }
      if (!isNaN(this.productId)){
        this.productService.getDetailProduct(this.productId).subscribe({
          next: (response: any) => {
            debugger
            if (response.product_images && response.product_images.length > 0) {
              response.product_images.forEach((product_images: ProductImage) => {
                product_images.image_url = `${environment.apiBaseUrl}/products/images/${product_images.image_url}`;
              });
            }
            debugger;
            this.product = response;
            this.showImage(0);
          },
          complete: () => {
            debugger;
          },
          error: (error : any) => {
            debugger;
            console.log("Error fetching detail",error);
          }
        });
      } else {
        console.log("Invalid productId: ",idParam);
      }
      // this.cartService.clearCart();
    }
    showImage(index: number){
      debugger;
      if (this.product && this.product.product_images
        && this.product.product_images.length > 0) {
        if (index<0){
          index = 0;
        } else {
          if (index >= this.product.product_images.length){
            index = this.product.product_images.length - 1;
          }
        }
        this.currentImageIndex = index;
      }
    }
    addToCart(){
      debugger;
      if (this.product){
        this.cartService.addToCart(this.product.id, this.quantity);
      } else {
        console.error("Cannot add product to cart because of null value");
      }
    }
    thumbnailClick(index: number){
      this.currentImageIndex = index;
    }
    nextImage(){
      debugger;
      this.showImage(this.currentImageIndex + 1);
    }
  previousImage() {
      debugger;
      this.showImage(this.currentImageIndex - 1);
  }
  increaseQuantity(){
      this.quantity++;
  }
  decreaseQuantity() {
      if (this.quantity>1){
        this.quantity--;
      }
  }

  buyNow(){
      debugger;
    if (this.product){
      this.cartService.clearCart();
      this.cartService.addToCart(this.product.id, this.quantity);
    } else {
      console.error("Cannot add product to cart because of null value");
    }
    this.route.navigate(['/orders']);
  }
  addComment(){

  }
  submitComment(){
      if (this.createComment.rating>0){
        debugger;
        const storedIdsJSON = localStorage.getItem("storedIds");
        // localStorage.setItem('productIdComment',this.productId.toString());
        if (storedIdsJSON&&storedIdsJSON.length>0) {
          const storedIds: number[] = JSON.parse(storedIdsJSON);
          this.commentService.createComment(storedIds[0], this.createComment).subscribe({
            next: (response: any) =>{
              debugger;
              console.log(response);
              alert("Post comment successfully!");
            },
            complete: () =>{
             debugger;
            },
            error: (error: any) => {
              debugger;
              console.log("Error fetching data: error "+error.error.message);
            }
          });
        } else {
          alert("You must buy this product to comment");
        }
      } else {
        alert("Please select 1 -> 5 star for this product");
      }
  }
  setRating(rating: number) {
    this.createComment.rating = rating;
  }
  getStarArray(rating: number): number[] {
    return Array.from({ length: rating }, (_, index) => index + 1);
  }
  sortComments(): void {
    this.afterSortComments = this.comments.sort((a, b) => {
      const dateA = new Date(a.created_at);
      const dateB = new Date(b.created_at);
      return dateB.getTime() - dateA.getTime();
    });
  }

  protected readonly Number = Number;
}
