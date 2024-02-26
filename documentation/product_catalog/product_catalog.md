# Product Catalog
For users, navigating the product catalog is an essential feature. Effortlessly locate products by category, price range, or relevant criteria, ensuring a user-friendly and efficient shopping process. On the administrative side, the ability to modify, organize, and customize the product range provides control and flexibility, presenting a dynamic and appealing catalog.

## Entity relations
```mermaid
erDiagram
  CATEGORY }o--o{ PRODUCT : contains
```

## Package relations
```mermaid
flowchart LR
  A[MODEL] --> B[REPOSITORY]
  C[SERVICE] --> B
  D[CONTROLLER] --> C
```

## Model Package
```mermaid
classDiagram
  Product "0..*" <-- "0..*" Category
  class Product {
    -productId : UUID
    -categoryId : UUID
    -name : String
    -description : String
    -price : int
    -inStock : int
  }
  class Category {
    -categoryId : UUID
    -name : String
    -description : String
    -parentId : UUID
  }
```

## Repository package
```mermaid
classDiagram
  class ProductRepository {
    +findByCategoryId(categoryId: UUID) List "Product"
  }
  class CategoryRepository {
  }
```

## Service package
```mermaid
classDiagram
  class ProductService {
    -productRerpository : ProductRepository
    +createProduct(product: Product) Product
    +deleteProduct(productId: UUID)
    +updateProduct(product: Product) Product
    +findProductById(productId: UUID) Product
    +findAllProduct() List "Product" 
  }
  class CategoryService {
    -categoryRepository : CategoryRepository
    -productRepository : ProductRepository
    +createCategory(category: Category) Category
    +deleteCategory(categoryId: UUID)
    +updateCategory(category: Category) Category
    +findCategoryById(categoryId: UUID) Category
    +getProducts(categoryId: UUID) List "Product"
    +findAllCategory() List "Category" 
  }
```

## Controller package
```mermaid
classDiagram
  class ProductController {
    -productService : ProductService
    +createProduct(productDto: ProductDto) ResponseEntity "ProductDto"
    +deleteProduct(productId: UUID) ResponseEntity "Unit"
    +updateProduct(productDto: ProductDto) ResponseEntity "ProductDto"
    +findProductById(productId: UUID) ResponseEntity "ProductDto"
    +findAllProduct() ResponseEntity "List "ProductDto"" 
  }
  class CategoryController {
    -categoryService : CategoryService
    +createCategory(categoryDto: CategoryDto) ResponseEntity "CategoryDto"
    +deleteCategory(categoryId: UUID) ResponseEntity "Unit"
    +updateCategory(categoryDto: CategoryDto) ResponseEntity "CategoryDto"
    +findCategoryById(categoryId: UUID) ResponseEntity "CategoryDto"
    +getProducts(categoryId: UUID) ResponseEntity "List "ProductDto""
    +findAllCategory() ResponseEntity "List "CategoryDto"" 
  }
```
## User Stories

As a customer, I want to be able to browse products by category, so that I can easily find items of interest.

As a customer, I want to be able to search for products by name or description, so that I can quickly locate specific items.

As a customer, I want to be able to view detailed information about a product, including its name, description, price, and availability, so that I can make an informed purchasing decision.

As a customer, I want to be able to filter products based on criteria such as price range, brand, or rating, so that I can narrow down my search results.

---

As an administrator, I want to be able to add new products to the catalog, including details such as name, description, price, and availability, so that the catalog remains up-to-date with the latest offerings.

As an administrator, I want to be able to update existing product information, such as price, availability, or description, so that I can make adjustments as needed.

As an administrator, I want to be able to organize products into categories and subcategories, so that customers can easily navigate and find products.

As an administrator, I want to be able to delete products from the catalog, ensuring that discontinued items are no longer displayed to customers.


