import {Component, OnInit} from '@angular/core';
import {Product} from "../../../../models/product";
import {ProductService} from "../../../../services/product.service";
import {CartService} from "../../../../services/cart.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductImage} from "../../../../models/product.image";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-detail-product-admin',
  templateUrl: './detail-product-admin.component.html',
  styleUrls: ['./detail-product-admin.component.scss']
})
export class DetailProductAdminComponent implements OnInit{
  product: Product = {
    name: '',
    thumbnail: '',
    id: 0,
    category_id: 0,
    description: '',
    price: 0,
    product_images: [],
    url: '',
}
  productId: number = 0;
  editButton = false;
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
    this.idDirect = Number(this.router.snapshot.paramMap.get('id'));
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
  saveProduct(){
    debugger;
    this.productService.updateProduct(this.product).subscribe({
      next: (response: any) => {
        debugger;
        alert(response.message);
        this.route.navigate(['../'], {relativeTo: this.router});
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        alert(error.error.message);
      }
    })
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
  clickEditButton(){
    this.editButton = true;
  }
}
