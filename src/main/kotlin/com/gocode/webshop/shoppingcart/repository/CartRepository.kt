package com.gocode.webshop.shoppingcart.repository

import com.gocode.webshop.shoppingcart.model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CartRepository: JpaRepository<Cart, UUID> {
    fun findByUserId(userId: UUID) : Cart?

}