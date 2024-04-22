package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.constants.DELETED
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.repository.UserRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val addressService: AddressService,
    //private val passwordEncoder: PasswordEncoder
) {
    fun findUserById(userId: UUID): User {
        return userRepository.findById(userId)
            .orElseThrow { throw EntityNotFoundException(userId.toString(), User::class.java) }
    }

    fun createUser(user: User): User {
        return userRepository.save(
            user.copy(
                id = null,
                password = user.password
            )
        )
    }

    fun deleteUser(userId: UUID): User {
        val user = findUserById(userId)
        if (user.deleted) {
            throw IllegalArgumentException("Can't delete already deleted user")
        }
        deleteAddresses(userId)
        return userRepository.save(
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

    fun updateUser(user: User): User {
        if (user.id == null) {
            throw IllegalArgumentException("Can't update user with null id")
        }
        val originalUser = findUserById(user.id)
        if (originalUser.deleted) {
            throw IllegalArgumentException("Can't update deleted user")
        }
        return userRepository.save(
            originalUser.copy(
                role = user.role,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                phoneNumber = user.phoneNumber,
            )
        )
    }

    fun changePassword(userId: UUID, password: String): User {
        val originalUser = findUserById(userId)
        if (originalUser.deleted) {
            throw IllegalArgumentException("Can't change password of deleted user")
        }
        return userRepository.save(
            originalUser.copy(
                password = password
            )
        )
    }

    fun getAddresses(userId: UUID): List<Address> {
        if (!userRepository.existsById(userId)) {
            throw EntityNotFoundException(userId.toString(), User::class.java)
        }
        return addressService.findByUserId(userId)
    }

    fun deleteAddresses(userId: UUID) {
        if (!userRepository.existsById(userId)) {
            throw EntityNotFoundException(userId.toString(), User::class.java)
        }
        val addresses = getAddresses(userId)
        addresses.map { addressService.deleteAddress(it) }
    }

    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun findAllNonDeletedUsers(): List<User> {
        return findAllUsers().filter { !it.deleted }
    }

}