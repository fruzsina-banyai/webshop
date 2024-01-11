package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.usermanagement.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.repository.UserRepository
import java.lang.IllegalArgumentException
import java.util.*

class UserService (
    private val userRepository : UserRepository
) {
    fun findUserById(id: UUID) : User {
        return userRepository.findById(id).orElseThrow { throw EntityNotFoundException(id.toString(), User::class.java) }
    }

    fun createUser(user: User): User {
        return userRepository.save(user.copy(id = null))
    }

    fun deleteUser(id: UUID) {
        val user = userRepository.findById(id).orElseThrow { throw EntityNotFoundException(id.toString(), User::class.java) }
        userRepository.save(user.copy(
            firstName = "deleted",
            lastName = "deleted",
            email = "deleted",
            phoneNumber = "deleted"
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

    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }
}