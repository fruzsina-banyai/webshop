package com.gocode.webshop.shoppingcart.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.errors.NoAvailableStockException
import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.productcatalog.service.ProductService
import com.gocode.webshop.shoppingcart.model.Cart
import com.gocode.webshop.shoppingcart.model.CartItem
import com.gocode.webshop.shoppingcart.model.CartItemId
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.service.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import java.util.*

private const val ANY_ROLE = "user"

private const val ANY_FIRST_NAME = "firstName"

private const val ANY_LAST_NAME = "lastName"

private const val ANY_PHONE_NUMBER = "phoneNumber"

private const val ANY_PASSWORD = "password"

private const val ANY_NAME = "name"

private const val ANY_DESCRIPTION = "description"

private const val ANY_PRICE = 1.234

private const val ANY_IN_STOCK = 1.234

private const val ANY_COUNT = 1.234

private const val HIGH_COUNT = Double.MAX_VALUE

private const val ZERO_COUNT = 0.00

private const val UPDATED_COUNT = 1.00

@ActiveProfiles("test")
@WithMockUser(roles = ["ADMIN"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartServiceIT {
    @Autowired
    lateinit var cartService : CartService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var productService: ProductService


    @Test
    fun `should create cart with user`() {
        val createdUser = userService.createUser(createUser())
        assertDoesNotThrow { cartService.findCartByUserId(createdUser.id!!) }
    }

    @Test
    fun `should add item to cart`() {
        val createdUser = userService.createUser(createUser())
        val createdProduct = productService.createProduct(createProduct())
        val createdCart = cartService.findCartByUserId(createdUser.id!!)

        val cartItem = createCartItem(createdCart, createdProduct)

        cartService.addItemToCart(cartItem)

        val dbCartItem = cartService.findCartItemById(cartItem.id)

        assertEquals(cartItem, dbCartItem)
    }

    @Test
    fun `should empty cart`() {
        val createdUser = userService.createUser(createUser())
        val createdProduct = productService.createProduct(createProduct())
        val createdCart = cartService.findCartByUserId(createdUser.id!!)

        cartService.addItemToCart(createCartItem(createdCart, createdProduct))
        cartService.emptyCart(createdCart.id!!)

        assertTrue(cartService.findCartItemsByCartId(createdCart.id!!).isEmpty())
    }

    @Test
    fun `should delete items by product id`() {
        val createdUser = userService.createUser(createUser())
        val createdProduct = productService.createProduct(createProduct())
        val createdCart = cartService.findCartByUserId(createdUser.id!!)

        cartService.addItemToCart(createCartItem(createdCart, createdProduct))
        cartService.deleteItemsByProductId(createdProduct.id!!)

        assertTrue(cartService.findCartItemsByProductId(createdProduct.id!!).isEmpty())
    }

    @Test
    fun `should remove item from cart`() {
        val createdUser = userService.createUser(createUser())
        val createdProduct = productService.createProduct(createProduct())
        val createdCart = cartService.findCartByUserId(createdUser.id!!)

        val cartItem = cartService.addItemToCart(createCartItem(createdCart, createdProduct))
        cartService.removeItemFromCart(cartItem.id)

        assertThrows<EntityNotFoundException> { cartService.findCartItemById(cartItem.id) }
    }

    @Test
    fun `should throw error on no available stock`() {
        val createdUser = userService.createUser(createUser())
        val createdProduct = productService.createProduct(createProduct())
        val createdCart = cartService.findCartByUserId(createdUser.id!!)

        val cartItem = cartService.addItemToCart(createCartItem(createdCart, createdProduct))
        assertThrows<NoAvailableStockException> { cartService.updateItemCountInCart(cartItem.copy(count = HIGH_COUNT)) }
    }

    @Test
    fun `should delete item on zero or less count`() {
        val createdUser = userService.createUser(createUser())
        val createdProduct = productService.createProduct(createProduct())
        val createdCart = cartService.findCartByUserId(createdUser.id!!)

        val cartItem = cartService.addItemToCart(createCartItem(createdCart, createdProduct))
        cartService.updateItemCountInCart(cartItem.copy(count = ZERO_COUNT))

        assertThrows<EntityNotFoundException> { cartService.findCartItemById(cartItem.id) }
    }

    @Test
    fun `should update item count in cart`() {
        val createdUser = userService.createUser(createUser())
        val createdProduct = productService.createProduct(createProduct())
        val createdCart = cartService.findCartByUserId(createdUser.id!!)

        val cartItem = cartService.addItemToCart(createCartItem(createdCart, createdProduct))
        cartService.updateItemCountInCart(cartItem.copy(count = UPDATED_COUNT))

        assertEquals(cartService.findCartItemById(cartItem.id).count, UPDATED_COUNT)
    }

    companion object {
        fun createUser(): User {
            return User(
                id = UUID.randomUUID(),
                role = ANY_ROLE,
                deleted = false,
                firstName = ANY_FIRST_NAME,
                lastName = ANY_LAST_NAME,
                email = UserServiceIT.generateRandomEmail(),
                phoneNumber = ANY_PHONE_NUMBER,
                password = ANY_PASSWORD
            )
        }

        fun createProduct(): Product {
            return Product(
                id = UUID.randomUUID(),
                categoryId = null,
                name = ANY_NAME,
                description = ANY_DESCRIPTION,
                price = ANY_PRICE,
                inStock = ANY_IN_STOCK,
            )
        }

        fun createCartItem(cart: Cart, product: Product): CartItem {
            return CartItem(
                id = CartItemId(cart.id!!, product.id!!),
                cart = cart,
                product = product,
                count = ANY_COUNT
            )
        }
    }
}
