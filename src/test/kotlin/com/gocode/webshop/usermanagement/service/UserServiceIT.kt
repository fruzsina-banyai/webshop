package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.User
import org.assertj.core.api.CollectionAssert.assertThatCollection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.IllegalArgumentException
import java.util.*

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

private const val ANY_COUNTRY = "country"

private const val ANY_STATE = "state"

private const val ANY_ZIP_CODE = "zipcode"

private const val ANY_CITY = "city"

private const val ANY_STREET_ADDRESS = "streetAddress"

private const val DELETED = "deleted"

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceIT {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var addressService: AddressService

//    @Autowired
//    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should save new user with null id`() {
        val user = createUser().copy(id = null)

        val createdUser = userService.createUser(user)
        val dbUser = userService.findUserById(createdUser.id!!)

        assertNotNull(createdUser.id)
        assertEquals(createdUser.id, dbUser.id)
//        assertTrue(passwordEncoder.matches(ANY_PASSWORD, dbUser.password))
    }

    @Test
    fun `should save user with any id`() {
        val user = createUser()

        val createdUser = userService.createUser(user)
        val dbUser = userService.findUserById(createdUser.id!!)

        assertNotNull(dbUser.id)
        assertEquals(createdUser.id, dbUser.id)
//        assertTrue(passwordEncoder.matches(ANY_PASSWORD, dbUser.password))
    }

    @Test
    fun `should throw error on deleted user when updating`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        userService.deleteUser(createdUser.id!!)

        assertThrows<IllegalArgumentException> { userService.updateUser(createdUser) }
    }

    @Test
    fun `should throw error on non-existent id when updating`() {
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
            deleted = true,
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
        assertEquals(createdUser.deleted, dbUser.deleted)
        assertEquals(updatedUser.firstName, dbUser.firstName)
        assertEquals(updatedUser.lastName, dbUser.lastName)
        assertEquals(updatedUser.email, dbUser.email)
        assertEquals(updatedUser.phoneNumber, dbUser.phoneNumber)
        assertEquals(createdUser.password, dbUser.password)
    }

    @Test
    fun `should throw error on already deleted user`(){
        val user = createUser()
        val createdUser = userService.createUser(user)

        userService.deleteUser(createdUser.id!!)

        assertThrows<IllegalArgumentException> { userService.deleteUser(createdUser.id!!) }
    }

    @Test
    fun `should only delete personal data`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        userService.deleteUser(createdUser.id!!)
        val dbUser = userService.findUserById(createdUser.id!!)

        val deletedData = DELETED + createdUser.id

        assertEquals(createdUser.id, dbUser.id)
        assertEquals(createdUser.role, dbUser.role)
        assertEquals(true, dbUser.deleted,)
        assertEquals(deletedData, dbUser.firstName)
        assertEquals(deletedData, dbUser.lastName)
        assertEquals(deletedData, dbUser.email)
        assertEquals(deletedData, dbUser.phoneNumber)
        assertEquals(deletedData, dbUser.password)
    }

    @Test
    fun `should delete addresses when deleting user`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        val address = createAddress(createdUser.id!!)
        val createdAddress = addressService.createAddress(address)

        userService.deleteUser(createdUser.id!!)

        val dbAddress = addressService.findAddressById(createdAddress.id!!)

        assertTrue(dbAddress.deleted)
        assertEquals(DELETED + createdAddress.id, dbAddress.streetAddress)
    }

    @Test
    fun `should throw error on deleted user when changing password`(){
        val user = createUser()
        val createdUser = userService.createUser(user)

        userService.deleteUser(createdUser.id!!)

        assertThrows<IllegalArgumentException> { userService.changePassword(createdUser.id!!, UPDATED_PASSWORD) }
    }

    @Test
    fun `should change password correctly`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        userService.changePassword(createdUser.id!!, UPDATED_PASSWORD)
//        val dbUser = userService.findUserById(createdUser.id!!)

//        assertTrue(passwordEncoder.matches(UPDATED_PASSWORD, dbUser.password))
    }

    @Test
    fun `should return addresses`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        val address = createAddress(createdUser.id!!)
        val createdAddress = addressService.createAddress(address)

        val result = userService.getAddresses(createdUser.id!!)

        assertThatCollection(result)
            .extracting("id")
            .containsExactlyInAnyOrder(createdAddress.id)
    }

    @Test
    fun `should return empty list if given user does not have any addresses`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        assertTrue(userService.getAddresses(createdUser.id!!).isEmpty())
    }

    @Test
    fun `should throw error on non existent user`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        val address = createAddress(createdUser.id!!)
        addressService.createAddress(address)

        assertThrows<EntityNotFoundException> { userService.getAddresses(user.id!!) }
    }

    @Test
    fun `should delete addresses`() {
        val user = createUser()
        val createdUser = userService.createUser(user)

        val address = createAddress(createdUser.id!!)
        val createdAddress = addressService.createAddress(address)

        userService.deleteAddresses(createdUser.id!!)

        val dbAddress = addressService.findAddressById(createdAddress.id!!)

        assertTrue(dbAddress.deleted)
        assertEquals(DELETED + dbAddress.id, dbAddress.streetAddress)
    }

    companion object {
        fun createUser(): User {
            return User(
                id = UUID.randomUUID(),
                role = ANY_ROLE,
                deleted = false,
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

        fun createAddress(userId: UUID): Address {
            return Address(
                id = UUID.randomUUID(),
                userId = userId,
                deleted = false,
                country = ANY_COUNTRY,
                state = ANY_STATE,
                zipCode = ANY_ZIP_CODE,
                city = ANY_CITY,
                streetAddress = ANY_STREET_ADDRESS
            )
        }
    }
}

