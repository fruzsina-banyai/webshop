package com.gocode.webshop.shoppingcart.model

import com.gocode.webshop.shoppingcart.dto.CartDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.UUID

@Entity
@Table(name = "cart")
data class Cart(
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    val id: UUID?,

    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    val userId: UUID
) {
    companion object;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Cart

        return userId == other.userId
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Cart(id=$id, userId=$userId')"
    }
}

fun Cart.Companion.fromCartDto(cartDto: CartDto): Cart {
    return Cart(
        id = cartDto.id,
        userId = cartDto.userId
    )
}

fun Cart.toCartDto(): CartDto {
    return CartDto(
        id = id,
        userId = userId
    )
}