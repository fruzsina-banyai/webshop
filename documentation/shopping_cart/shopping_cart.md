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
    -id : UUID
    -userId : UUID
  }
  class CartItem {
    -id : CartItemId
    -cart : Cart
    -product : Product
    -count : Double
  }
  class CartItemId {
    -cartId : UUID
    -productId : UUID
  }
```

## Repository package
```mermaid
classDiagram
  class CartRepository {
    +findByUserId(userId: UUID) Cart
  }
  class CartItemRepository {
    +findAllByIdCartId(cartId: UUID) List"CartItem"
    +findAllByIdProductId(productId: UUID) List"CartItem"
  }
```

## Service package
```mermaid
classDiagram
  class CartService{
    -cartRepository : CartRepository
    -cartItemRepository : CartItemRepository
    -userService : UserService 
    +findCartById(cartId: UUID) Cart 
    +findCartByUserId(userId: UUID) Cart 
    +findCartItemById(cartItemId: CartItemId) CartItem 
    +findCartItemsByProductId(productId: UUID) List"CartItem"
    +findCartItemsByCartId(cartId: UUID) List"CartItem"
    +emptyCart(cartId: UUID)
    +addItemToCart(cartItem: CartItem) CartItem 
    +deleteItemsByProductId(productId: UUID) 
    +removeItemFromCart(cartItemId: CartItemId) 
    +updateItemCountInCart(cartItem: CartItem) 
  }
```

## Controller package
```mermaid
classDiagram
  class CartController{
    -cartService : CartService
    +findCartById(cartId: UUID) ResponseEntity"CartDto"
    +findCartByUserId(userId: UUID) ResponseEntity"CartDto" 
    +findCartItemById(cartItemId: CartItemId) "CartItemDto" 
    +findCartItemsByProductId(productId: UUID) ResponseEntity"List"CartItemDto"" 
    +findCartItemsByCartId(cartId: UUID) ResponseEntity"List"CartItem""
    +emptyCart(cartId: UUID) ResponseEntity"Unit"
    +addItemToCart(cartItem: CartItem) ResponseEntity"CartItemDto" 
    +deleteItemsByProductId(productId: UUID) ResponseEntity"Unit"
    +removeItemFromCart(cartItemId: CartItemId) ResponseEntity"Unit"
    +updateItemCountInCart(cartItem: CartItem) ResponseEntity"Unit"
  }
```

## User stories

As a customer, I want to be able to add products to my shopping cart so that I can purchase them later.

As a customer, I want to be able to view the contents of my shopping cart so that I can see what items I have selected.

As a customer, I want to be able to remove products from my shopping cart so that I can change my selection before checking out.

As a customer, I want to be able to clear my entire shopping cart so that I can start over with a new selection.

As a customer, I want to be able to proceed to checkout from the shopping cart so that I can complete my purchase.

As a registered customer, I want to be able to save my shopping cart for later so that I can return to it at another time.
