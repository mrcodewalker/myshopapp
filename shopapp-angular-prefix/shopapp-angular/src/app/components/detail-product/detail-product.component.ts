import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/product";
import {ProductService} from "../../services/product.service";
import {environment} from "../../environments/environment";
import {ProductImage} from "../../models/product.image";
import {CartService} from "../../services/cart.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthGuard} from "../../guards/auth.guard";

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss']
})
export class DetailProductComponent implements OnInit{
    product?: Product;
    productId: number = 0;
    quantity: number = 1;
    idDirect: number = 1;
    currentImageIndex: number = 0;
    constructor(
      private productService: ProductService,
      private cartService: CartService,
      private router: ActivatedRoute,
      private route: Router
      // private categoryService: CategoryService,
      // private router: Router,
      // private activatedRoute: ActivatedRoute
    ) {
    }
    ngOnInit(): void {
      this.router.params.subscribe(params => {
        this.idDirect = params['id'];
        debugger;
      });
      const idParam = this.idDirect;
      debugger
      // this.cartService.clearCart();
      if (idParam !== null){
        this.productId = +idParam;
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
}
