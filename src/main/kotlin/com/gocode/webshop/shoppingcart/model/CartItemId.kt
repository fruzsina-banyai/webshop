package com.gocode.webshop.shoppingcart.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.UUID

@Embeddable
data class CartItemId (
    @Column(name = "cart_id", unique = true, nullable = false, updatable = false)
    val cartId: UUID,

    @Column(name = "product_id", unique = true, nullable = false, updatable = false)
    val productId: UUID
) : Serializable