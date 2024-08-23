package com.gocode.webshop.shoppingcart.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.errors.NoAvailableStockException
import com.gocode.webshop.shoppingcart.model.Cart
import com.gocode.webshop.shoppingcart.model.CartItem
import com.gocode.webshop.shoppingcart.model.CartItemId
import com.gocode.webshop.shoppingcart.repository.CartItemRepository
import com.gocode.webshop.shoppingcart.repository.CartRepository
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val userService: UserService,
) {
    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.canCurrentUserAccessCart(#cartId)")
    fun findCartById(cartId: UUID): Cart {
        return cartRepository.findById(cartId)
            .orElseThrow { throw EntityNotFoundException(cartId.toString(), Cart::class.java) }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == @userService.getCurrentUserId()")
    fun findCartByUserId(userId: UUID): Cart {
        return cartRepository.findByUserId(userId) ?: throw (EntityNotFoundException(
            userId.toString(),
            User::class.java
        ))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.canCurrentUserAccessCart(#cartItemId.id.cartId)")
    fun findCartItemById(cartItemId: CartItemId): CartItem {
        return cartItemRepository.findById(cartItemId)
            .orElseThrow { throw EntityNotFoundException(cartItemId.toString(), CartItem::class.java) }
    }

    private fun CartItemId.existsById(): Boolean {
        return cartItemRepository.existsById(this)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun findCartItemsByProductId(productId: UUID): List<CartItem> {
        return cartItemRepository.findAllByIdProductId(productId)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.canCurrentUserAccessCart(#cartId)")
    fun findCartItemsByCartId(cartId: UUID): List<CartItem> {
        return cartItemRepository.findAllByIdCartId(cartId)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.canCurrentUserAccessCart(#cartItem.id.cartId)")
    fun addItemToCart(cartItem: CartItem): CartItem {
        return cartItemRepository.save(cartItem)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.canCurrentUserAccessCart(#cartId)")
    fun emptyCart(cartId: UUID) {
        findCartItemsByCartId(cartId).deleteCartItems()
    }

    fun deleteItemsByProductId(productId: UUID) {
        findCartItemsByProductId(productId).deleteCartItems()
    }

    private fun List<CartItem>.deleteCartItems() {
        if (this.isNotEmpty()) {
            cartItemRepository.deleteAll(this)
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.canCurrentUserAccessCart(#cartItem.id.cartId)")
    fun removeItemFromCart(cartItemId: CartItemId) {
        if (cartItemId.existsById()) {
            cartItemRepository.deleteById(cartItemId)
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userService.canCurrentUserAccessCart(#cartItem.id.cartId)")
    fun updateItemCountInCart(cartItem: CartItem) {
        if (cartItem.id.existsById()) {
            if (!cartItem.isStockAvailable()) {
                throw NoAvailableStockException(cartItem.id.productId.toString())
            }
            if (cartItem.isCountLessThenZero()) {
                cartItemRepository.delete(cartItem)
            } else {
                cartItemRepository.save(cartItem.copy(count = cartItem.count))
            }
        }
    }

    private fun CartItem.isStockAvailable(): Boolean {
        return this.count < this.product.inStock
    }

    private fun CartItem.isCountLessThenZero(): Boolean {
        return this.count <= 0.0
    }
}