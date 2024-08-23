package com.gocode.webshop.shoppingcart.controller

import com.gocode.webshop.shoppingcart.dto.*
import com.gocode.webshop.shoppingcart.model.*
import com.gocode.webshop.shoppingcart.service.CartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService
) {
    @GetMapping("/{cartId}")
    fun findCartById(@PathVariable cartId: UUID): ResponseEntity<CartDto> {
        return ResponseEntity
            .ok()
            .body(cartService.findCartById(cartId).toCartDto())
    }

    @GetMapping("/by-user/{userId}")
    fun findByUserId(@PathVariable userId: UUID): ResponseEntity<CartDto> {
        return ResponseEntity
            .ok()
            .body(cartService.findCartByUserId(userId).toCartDto())
    }

    @GetMapping("/cart-item/{cartItemId}")
    fun findCartItemById(@PathVariable cartItemId: CartItemId): ResponseEntity<CartItemDto> {
        return ResponseEntity
            .ok()
            .body(cartService.findCartItemById(cartItemId).toCartItemDto())
    }

    @GetMapping("/cart-item/by-cart/{cartId}")
    fun findCartItemsByCartId(@PathVariable cartId: UUID): ResponseEntity<List<CartItemDto>> {
        return ResponseEntity
            .ok()
            .body(cartService.findCartItemsByCartId(cartId).map { it.toCartItemDto() }.toList())
    }

    @PostMapping
    fun addItemToCart(@RequestBody cartItemDto: CartItemDto): ResponseEntity<CartItemDto> {
        return ResponseEntity
            .ok()
            .body(cartService.addItemToCart(CartItem.fromCartItemDto(cartItemDto)).toCartItemDto())
    }

    @DeleteMapping("/empty/{cartId}")
    fun emptyCart(@PathVariable cartId: UUID): ResponseEntity<Unit> {
        return ResponseEntity
            .ok()
            .body(cartService.emptyCart(cartId))
    }

    @DeleteMapping("/cart-item/{cartItemId}")
    fun removeItemFromCart(@PathVariable cartItemId: CartItemId): ResponseEntity<Unit> {
        return ResponseEntity
            .ok()
            .body(cartService.removeItemFromCart(cartItemId))
    }

    @PutMapping
    fun updateItemCountInCart(@RequestBody cartItemDto: CartItemDto): ResponseEntity<Unit> {
        return ResponseEntity
            .ok()
            .body(cartService.updateItemCountInCart(CartItem.fromCartItemDto(cartItemDto)))

    }    }