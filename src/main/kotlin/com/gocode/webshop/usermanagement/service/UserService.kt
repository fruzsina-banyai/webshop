package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.usermanagement.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.repository.AddressRepository
import com.gocode.webshop.usermanagement.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class UserService (
    private val userRepository : UserRepository,
    private val addressRepository: AddressRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun findUserById(userId: UUID) : User {
        return userRepository.findById(userId).orElseThrow { throw EntityNotFoundException(userId.toString(), User::class.java) }
    }

    fun createUser(user: User): User {
        return userRepository.save(user.copy(
            id = null,
            password = passwordEncoder.encode(user.password)
        ))
    }

    fun deleteUser(userId: UUID) {
        val user = userRepository.findById(userId).orElseThrow { throw EntityNotFoundException(userId.toString(), User::class.java) }
        userRepository.save(user.copy(
            firstName = "deleted",
            lastName = "deleted",
            email = "deleted",
            phoneNumber = "deleted",
            password = "deleted"
        ))
    }

    fun updateUser(user: User): User {
        if (user.id == null) {
            throw IllegalArgumentException("Can't update user with null id")
        }
        val originalUser = findUserById(user.id)
        return userRepository.save(originalUser.copy(
            role = user.role,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phoneNumber = user.phoneNumber,
        ))
    }

    fun changePassword(userId: UUID, password: String): User {
        val originalUser = findUserById(userId)
        return userRepository.save(originalUser.copy(
            password = passwordEncoder.encode(password)
        ))
    }

    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getAddresses(userId: UUID): List<Address> {
        return addressRepository.findByUserId(userId).takeIf { it.isNotEmpty() } ?: throw EntityNotFoundException(userId.toString(), User::class.java)
    }

}