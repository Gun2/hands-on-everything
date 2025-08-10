export type Product = {
  id: number;
  name: string;
  price: number;
  createdAt: string;
  updatedAt: string;
}

/**
 * 상품 생성
 */
export type ProductCreateRequest = {
  name: Product["name"];
  price: Product["price"];
}

/**
 * 상품 수정
 */
export type ProductUpdateRequest = ProductCreateRequest;