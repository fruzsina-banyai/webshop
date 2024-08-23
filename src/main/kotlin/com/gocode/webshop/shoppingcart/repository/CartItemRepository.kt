package com.gocode.webshop.shoppingcart.repository

import com.gocode.webshop.shoppingcart.model.CartItem
import com.gocode.webshop.shoppingcart.model.CartItemId
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CartItemRepository : JpaRepository<CartItem, CartItemId> {
    fun findAllByIdProductId(productId: UUID): List<CartItem>

    fun findAllByIdCartId(cartId: UUID): List<CartItem>
}