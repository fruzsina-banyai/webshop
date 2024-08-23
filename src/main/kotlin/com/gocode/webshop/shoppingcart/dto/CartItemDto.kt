package com.gocode.webshop.shoppingcart.dto

import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.shoppingcart.model.Cart
import com.gocode.webshop.shoppingcart.model.CartItemId

data class CartItemDto(
    val id: CartItemId,
    val cart: Cart,
    val product: Product,
    val count: Double
)
