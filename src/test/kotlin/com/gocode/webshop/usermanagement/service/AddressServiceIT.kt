package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.usermanagement.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

private const val ANY_ROLE = "user"

private const val ANY_FIRST_NAME = "firstName"

private const val ANY_LAST_NAME = "lastName"

private const val ANY_PHONE_NUMBER = "phoneNumber"

private const val ANY_PASSWORD = "password"

private const val ANY_COUNTRY = "country"

private const val ANY_STATE = "state"

private const val ANY_ZIP_CODE = "zipcode"

private const val ANY_CITY = "city"

private const val ANY_STREET_ADDRESS = "streetAddress"

private const val UPDATED_COUNTRY = "updatedCountry"

private const val UPDATED_STATE = "updatedState"

private const val UPDATED_ZIP_CODE = "updatedZipCode"

private const val UPDATED_CITY = "updatedCity"

private const val UPDATED_STREET_ADDRESS = "updatedStreetAddress"

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressServiceIT {
    @Autowired
    lateinit var addressService: AddressService

    @Autowired
    lateinit var userService: UserService

    @Test
    fun `should save new address with null id`() {
        val user = userService.createUser(createUser())
        val address = createAddress(user.id!!).copy(id = null)

        val createdAddress = addressService.createAddress(address)
        val dbAddress = addressService.findAddressById(createdAddress.id!!)

        Assertions.assertNotNull(createdAddress.id)
        assertEquals(createdAddress.id, dbAddress.id)
    }

    @Test
    fun `should save user with any id`() {
        val user = userService.createUser(createUser())
        val address = createAddress(user.id!!)

        val createdAddress = addressService.createAddress(address)
        val dbAddress = addressService.findAddressById(createdAddress.id!!)

        Assertions.assertNotNull(createdAddress.id)
        assertEquals(createdAddress.id, dbAddress.id)
    }

    @Test
    fun `should throw error on non-existent id`() {
        val user = userService.createUser(createUser())
        val address = createAddress(user.id!!)
        addressService.createAddress(address)

        assertThrows<EntityNotFoundException> { addressService.updateAddress(address) }
    }

    @Test
    fun `should only save updatable fields`() {
        val user = userService.createUser(createUser())
        val address = createAddress(user.id!!)

        val createdAddress = addressService.createAddress(address)
        val updatedAddress = createAddress(user.id!!).copy(
            id = createdAddress.id,
            userId = UUID.randomUUID(),
            country = UPDATED_COUNTRY,
            state = UPDATED_STATE,
            zipCode = UPDATED_ZIP_CODE,
            city = UPDATED_CITY,
            streetAddress = UPDATED_STREET_ADDRESS
        )

        addressService.updateAddress(updatedAddress)
        val dbAddress = addressService.findAddressById(createdAddress.id!!)

        assertEquals(createdAddress.id, dbAddress.id)
        assertEquals(createdAddress.userId, dbAddress.userId)
        assertEquals(updatedAddress.country, dbAddress.country)
        assertEquals(updatedAddress.state, dbAddress.state)
        assertEquals(updatedAddress.zipCode, dbAddress.zipCode)
        assertEquals(updatedAddress.city, dbAddress.city)
        assertEquals(updatedAddress.streetAddress, dbAddress.streetAddress)
    }

    @Test
    fun `should delete address`() {
        val user = userService.createUser(createUser())
        val address = createAddress(user.id!!)

        val createdAddress = addressService.createAddress(address)
        addressService.deleteAddress(createdAddress.id!!)

        assertThrows<EntityNotFoundException> { addressService.findAddressById(createdAddress.id!!) }
    }

    companion object{
        fun createAddress(userId: UUID): Address {
            return Address(
                id = UUID.randomUUID(),
                userId = userId,
                country = ANY_COUNTRY,
                state = ANY_STATE,
                zipCode = ANY_ZIP_CODE,
                city = ANY_CITY,
                streetAddress = ANY_STREET_ADDRESS

            )
        }

        private fun createUser(): User {
            return User(
                id = UUID.randomUUID(),
                role = ANY_ROLE,
                firstName = ANY_FIRST_NAME,
                lastName = ANY_LAST_NAME,
                email = UserServiceIT.generateRandomEmail(),
                phoneNumber = ANY_PHONE_NUMBER,
                password = ANY_PASSWORD
            )
        }
    }
}