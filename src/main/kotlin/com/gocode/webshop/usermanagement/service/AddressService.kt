package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.constants.DELETED
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.repository.AddressRepository
import com.gocode.webshop.usermanagement.repository.UserRepository
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.UUID

@Service
class AddressService (
    private val addressRepository : AddressRepository,
    private val userRepository: UserRepository
) {
    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.userId == @userService.getCurrentUserId()")
    fun findAddressById(addressId: UUID) : Address {
        return addressRepository.findById(addressId).orElseThrow { throw EntityNotFoundException(addressId.toString(), Address::class.java) }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == @userService.getCurrentUserId()")
    fun findByUserId(userId: UUID) : List<Address> {
        return addressRepository.findByUserId(userId)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #address.userId == @userService.getCurrentUserId()")
    fun createAddress(address: Address) : Address {
        if (isUserDeleted(address.userId)) {
            throw IllegalArgumentException("Can't create address for deleted user")
        }
        return addressRepository.save(address.copy(id=null))
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.userId == @userService.getCurrentUserId()")
    fun deleteAddress(addressId: UUID) : Address {
        val address = findAddressById(addressId)
        if (address.deleted) {
            throw IllegalArgumentException("Can't delete already deleted address")
        }
        return addressRepository.save(address.copy(
            streetAddress = DELETED + addressId,
            deleted = true,
        ))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #address.userId == @userService.getCurrentUserId()")
    fun deleteAddress(address: Address) : Address {
        if (address.deleted) {
            throw IllegalArgumentException("Can't delete already deleted address")
        }
        return addressRepository.save(address.copy(
            streetAddress = DELETED + address.id,
            deleted = true,
        ))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #address.userId == @userService.getCurrentUserId()")
    fun updateAddress(address: Address) : Address {
        if (address.id == null) {
            throw IllegalArgumentException("Can't update address with null id")
        }
        val originalAddress = findAddressById(address.id)
        if (originalAddress.deleted) {
            throw IllegalArgumentException("Can't update deleted address")
        }
        return addressRepository.save(originalAddress.copy(
            country = address.country,
            state = address.state,
            zipCode = address.zipCode,
            city = address.city,
            streetAddress = address.streetAddress
        ))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun findAllAddresses(): List<Address> {
        return addressRepository.findAll()
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun findAllNonDeletedAddresses(): List<Address> {
        return findAllAddresses().filter { !it.deleted }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == @userService.getCurrentUserId()")
    fun isUserDeleted(userId: UUID): Boolean {
        val user = userRepository.findById(userId).orElseThrow { EntityNotFoundException(userId.toString(), User::class.java) }
        return user.deleted
    }
}