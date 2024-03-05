import {ProductImage} from "./product.image";

export interface Product {
  name: string;
  price: number;
  thumbnail: string;
  url: string;
  description: string;
  category_id: number;
  id: number;
  product_images: ProductImage[];
}
