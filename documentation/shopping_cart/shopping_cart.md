# Shopping Cart

The shopping cart feature should not only collect various products chosen to be purchased but also displays selected items, quantities, and prices. This element simplifies the decision-making process, aiding users in finalizing their selections before proceeding to checkout.

## Entity relations

```mermaid
erDiagram
  CART }o--o{ PRODUCT : contains
  USER |o--|| CART : has
```

## Relations to other services

```mermaid
flowchart LR
  A[PRODUCT CATALOG] <--> B[SHOPPING CART]
  C[USER MANAGEMENT SERVICE] <--> B
```

## Package relations
```mermaid
flowchart LR
  A[MODEL] --> B[REPOSITORY]
  C[SERVICE] --> B
  D[CONTROLLER] --> C
```

## Model package
```mermaid
classDiagram
  class Cart {
    -cartId : UUID
    -userId : UUID
    -productIds : List "UUID"
  }
```

## Repository package
```mermaid
classDiagram
  class CartRepository {
    +findByUserId(userId: UUID) Cart
  }
```

## Service package
```mermaid
classDiagram
  class CartSerevice{
    -userService : UserService
    -productService : ProductService
    +createCart(cart: Cart) Cart
    +deleteCart(cartId: UUID)
    +emptyCart(cartId: UUID) Cart
    +findCartById(cartId: UUID) Cart
    +getProducts(cartId: UUID) List "UUID"
    +addProductsToCart(products: List "UUID") Cart
    +removeProductsFromCart(products: List "UUID") Cart
    +calculatePriceOfProducts(cartId: UUID) Double
  }
```

## Controller package
```mermaid
classDiagram
  class CartController{
    -cartService : CartService
    +createCart(cartDto: CartDto) ResponseEntity "CartDto"
    +deleteCart(cartDto: CartDto) ResponseEntity "Unit"
    +emptyCart(cartId: UUID) ResponseEntity "CartDto"
    +findCartById(cartId: UUID) ResponseEntity "CartDto"
    +addProductsToCart(products: List"UUID") ResponseEntity "CartDto"
    +removeProductsFromCart(products: List"UUID") ResponseEntity "CartDto"
    +calculatePriceOfProducts(cartId: UUID) ResponseEntity "Double"
  }
```

## User stories

As a customer, I want to be able to add products to my shopping cart so that I can purchase them later.

As a customer, I want to be able to view the contents of my shopping cart so that I can see what items I have selected.

As a customer, I want to be able to remove products from my shopping cart so that I can change my selection before checking out.

As a customer, I want to be able to clear my entire shopping cart so that I can start over with a new selection.

As a customer, I want to be able to proceed to checkout from the shopping cart so that I can complete my purchase.

As a registered customer, I want to be able to save my shopping cart for later so that I can return to it at another time.
