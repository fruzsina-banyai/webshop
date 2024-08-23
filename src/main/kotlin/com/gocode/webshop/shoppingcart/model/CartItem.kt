package com.gocode.webshop.shoppingcart.model

import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.shoppingcart.dto.CartItemDto
import jakarta.persistence.*
import jakarta.persistence.CascadeType.REFRESH
import org.hibernate.Hibernate

@Entity
@Table(name = "cart_items")
data class CartItem(
    @EmbeddedId
    val id: CartItemId,

    @ManyToOne(cascade = [REFRESH])
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    val cart: Cart,

    @ManyToOne(cascade = [REFRESH])
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    val product: Product,

    @Column(name = "count", nullable = false)
    val count: Double
) {
    companion object;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CartItem

        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "CartItem(id=$id, cart=$cart, product='$product', count='$count')"
    }
}

fun CartItem.Companion.fromCartItemDto(cartItemDto: CartItemDto): CartItem {
    return CartItem(
        id = cartItemDto.id,
        cart = cartItemDto.cart,
        product = cartItemDto.product,
        count = cartItemDto.count
    )
}

fun CartItem.toCartItemDto(): CartItemDto {
    return CartItemDto(
        id = id,
        cart = cart,
        product = product,
        count = count
    )
}