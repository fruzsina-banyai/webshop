package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.usermanagement.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.*

private const val DELETED_DATA = "deleted"

private const val ANY_ROLE = "user"

private const val ANY_FIRST_NAME = "firstName"

private const val ANY_LAST_NAME = "lastName"

private const val ANY_PHONE_NUMBER = "phoneNumber"

private const val ANY_PASSWORD = "password"

private const val UPDATED_ROLE = "updatedUser"

private const val UPDATED_FIRST_NAME = "updatedFirstName"

private const val UPDATED_LAST_NAME = "updatedLastName"

private const val UPDATED_PHONE_NUMBER = "updatedPhoneNumber"

private const val UPDATED_PASSWORD = "updatedPassword"

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceIT {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should save new user with null id`() {
        val user = createUser().copy(id = null)

        val createdUser = userService.createUser(user)
        val dbUser = userService.findUserById(createdUser.id!!)

        assertNotNull(createdUser.id)
        assertEquals(createdUser.id, dbUser.id)
        assertTrue(passwordEncoder.matches(ANY_PASSWORD, dbUser.password))
    }

    @Test
    fun `should save user with any id`() {
        val user = createUser()

        val createdUser = userService.createUser(user)
        val dbUser = userService.findUserById(createdUser.id!!)

        assertNotNull(dbUser.id)
        assertEquals(dbUser.id, createdUser.id)
        assertTrue(passwordEncoder.matches(ANY_PASSWORD, dbUser.password))
    }

    @Test
    fun `should throw error on non-existent id`() {
        val user = createUser()
        userService.createUser(user)

        assertThrows<EntityNotFoundException> { userService.updateUser(user) }
    }

    @Test
    fun `should only save updatable fields`() {
        val user = createUser()

        val createdUser = userService.createUser(user)
        val updatedUser = createUser().copy(
            id = createdUser.id,
            role = UPDATED_ROLE,
            firstName = UPDATED_FIRST_NAME,
            lastName = UPDATED_LAST_NAME,
            email = generateRandomEmail(),
            phoneNumber = UPDATED_PHONE_NUMBER,
            password = UPDATED_PASSWORD
        )

        userService.updateUser(updatedUser)
        val dbUser = userService.findUserById(createdUser.id!!)

        assertEquals(createdUser.id, dbUser.id)
        assertEquals(updatedUser.role, dbUser.role)
        assertEquals(updatedUser.firstName, dbUser.firstName)
        assertEquals(updatedUser.lastName, dbUser.lastName)
        assertEquals(updatedUser.email, dbUser.email)
        assertEquals(updatedUser.phoneNumber, dbUser.phoneNumber)
        assertEquals(createdUser.password, dbUser.password)
    }

    @Test
    fun `should only delete personal data`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        userService.deleteUser(createdUser.id!!)
        val dbUser = userService.findUserById(createdUser.id!!)

        assertEquals(dbUser.id, createdUser.id)
        assertEquals(dbUser.role, createdUser.role)
        assertEquals(dbUser.firstName, DELETED_DATA)
        assertEquals(dbUser.lastName, DELETED_DATA)
        assertEquals(dbUser.email, DELETED_DATA)
        assertEquals(dbUser.phoneNumber, DELETED_DATA)
        assertEquals(dbUser.password, DELETED_DATA)
    }

    @Test
    fun `should change password correctly`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        userService.changePassword(createdUser.id!!, UPDATED_PASSWORD)
        val dbUser = userService.findUserById(createdUser.id!!)

        assertTrue(passwordEncoder.matches(UPDATED_PASSWORD, dbUser.password))
    }



    companion object {
        fun createUser(): User {
            return User(
                id = UUID.randomUUID(),
                role = ANY_ROLE,
                firstName = ANY_FIRST_NAME,
                lastName = ANY_LAST_NAME,
                email = generateRandomEmail(),
                phoneNumber = ANY_PHONE_NUMBER,
                password = ANY_PASSWORD
            )
        }

        fun generateRandomEmail(): String {
            return "${UUID.randomUUID()}@email.com"
        }
    }
}

