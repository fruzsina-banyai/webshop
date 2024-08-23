package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.constants.DELETED
import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.errors.EmailAlreadyInUseException
import com.gocode.webshop.errors.UserNotLoggedInException
import com.gocode.webshop.shoppingcart.model.Cart
import com.gocode.webshop.shoppingcart.repository.CartRepository
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.repository.UserRepository
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val addressService: AddressService,
    private val cartRepository: CartRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun getCurrentUserId(): UUID? {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as? UserDetails
        return userDetails?.username?.let { userRepository.findByEmail(it)?.id } ?: throw UserNotLoggedInException()
    }

    fun canCurrentUserAccessCart(cartId: UUID): Boolean {
        val currentUserId = getCurrentUserId() ?: throw UserNotLoggedInException()
        val cart = cartRepository.findByUserId(currentUserId) ?: throw EntityNotFoundException(
            cartId.toString(),
            Cart::class.java
        )
        return cart.userId == currentUserId
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.email == authentication.principal.username")
    fun findUserById(userId: UUID): User {
        return userRepository.findById(userId)
            .orElseThrow { throw EntityNotFoundException(userId.toString(), User::class.java) }
    }

    fun existsById(userId: UUID): Boolean {
        return userRepository.existsById(userId)
    }

    fun User.existsByEmail(): Boolean {
        return userRepository.existsByEmail(this.email)
    }

    fun findByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw EntityNotFoundException(email, User::class.java)
    }

    fun createUser(user: User): User {
        if (user.existsByEmail()) {
            throw EmailAlreadyInUseException(user.id.toString())
        }
        val createdUser = userRepository.save(
            user.copy(
                id = null,
                password = passwordEncoder.encode(user.password)
            )
        )
        createCart(createdUser.id!!)
        return createdUser
    }

    private fun createCart(userId: UUID): Cart {
        if (!existsById(userId)) {
            throw EntityNotFoundException(userId.toString(), User::class.java)
        }
        return cartRepository.save(Cart(id = null, userId = userId))
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.email == authentication.principal.username")
    fun deleteUser(userId: UUID) {
        val user = findUserById(userId)
        if (!user.deleted) {
            deleteAddresses(userId)
            userRepository.save(
                user.copy(
                    deleted = true,
                    firstName = DELETED + user.id,
                    lastName = DELETED + user.id,
                    email = DELETED + user.id,
                    phoneNumber = DELETED + user.id,
                    password = DELETED + user.id
                )
            )
        }
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.email == authentication.principal.username")
    fun updateUser(user: User): User {
        if (user.id == null) {
            throw IllegalArgumentException("Can't update user with null id")
        }
        val originalUser = findUserById(user.id)
        if (originalUser.deleted) {
            throw IllegalArgumentException("Can't update deleted user")
        }
        if (originalUser.email != user.email && user.existsByEmail()) {
            throw EmailAlreadyInUseException(user.id.toString())
        }
        return userRepository.save(
            originalUser.copy(
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                phoneNumber = user.phoneNumber,
            )
        )
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.email == authentication.principal.username")
    fun changePassword(userId: UUID, password: String): User {
        val originalUser = findUserById(userId)
        if (originalUser.deleted) {
            throw IllegalArgumentException("Can't change password of deleted user")
        }
        return userRepository.save(
            originalUser.copy(
                password = passwordEncoder.encode(password)
            )
        )
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == @userService.getCurrentUserId()")
    fun getAddresses(userId: UUID): List<Address> {
        if (!userRepository.existsById(userId)) {
            throw EntityNotFoundException(userId.toString(), User::class.java)
        }
        return addressService.findByUserId(userId)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == @userService.getCurrentUserId()")
    fun deleteAddresses(userId: UUID) {
        if (!userRepository.existsById(userId)) {
            throw EntityNotFoundException(userId.toString(), User::class.java)
        }
        val addresses = getAddresses(userId)
        addresses.map { addressService.deleteAddress(it) }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun findAllNonDeletedUsers(): List<User> {
        return findAllUsers().filter { !it.deleted }
    }
}