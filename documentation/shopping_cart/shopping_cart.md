# Shopping Cart

The shopping cart feature should not only collect various products chosen to be purchased but also displays selected items, quantities, and prices. This element simplifies the decision-making process, aiding users in finalizing their selections before proceeding to checkout.

## Entity relations

```mermaid
erDiagram
  CART }o--o{ PRODUCT : contains
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
    -price : Double
  }
```




