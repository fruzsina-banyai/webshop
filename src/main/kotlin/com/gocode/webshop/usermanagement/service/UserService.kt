package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.repository.AddressRepository
import com.gocode.webshop.usermanagement.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

private const val DELETED = "deleted"

@Service
class UserService(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun findUserById(userId: UUID): User {
        return userRepository.findById(userId)
            .orElseThrow { throw EntityNotFoundException(userId.toString(), User::class.java) }
    }

    fun createUser(user: User): User {
        return userRepository.save(
            user.copy(
                id = null,
                password = passwordEncoder.encode(user.password)
            )
        )
    }

    fun deleteUser(userId: UUID): User {
        val user = userRepository.findById(userId)
            .orElseThrow { throw EntityNotFoundException(userId.toString(), User::class.java) }
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
        return userRepository.save(
            originalUser.copy(
                password = passwordEncoder.encode(password)
            )
        )
    }

    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getAddresses(userId: UUID): List<Address> {
        if (userRepository.existsById(userId)) {
            return addressRepository.findByUserId(userId)
        } else {
            throw EntityNotFoundException(userId.toString(), User::class.java)
        }
    }

    fun deleteAddresses(userId: UUID) {
        if (userRepository.existsById(userId)) {
            val addresses = getAddresses(userId)
            addresses.map { addressRepository.save(it.copy(
                streetAddress = DELETED + it.id,
                deleted = true
            )) }
        } else {
            throw EntityNotFoundException(userId.toString(), User::class.java)
        }
    }

}