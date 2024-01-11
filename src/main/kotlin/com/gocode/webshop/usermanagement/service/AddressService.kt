package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.usermanagement.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.repository.AddressRepository
import java.lang.IllegalArgumentException
import java.util.UUID

class AddressService (
    private val addressRepository : AddressRepository
) {
    fun findAddressById(id: UUID) : Address {
        return addressRepository.findById(id).orElseThrow { throw EntityNotFoundException(id.toString(), Address::class.java) }
    }

    fun createAddress(address: Address) : Address {
        return addressRepository.save(address.copy(id=null))
    }

    fun deleteAddress(id: UUID) {
        addressRepository.deleteById(id)
    }

    fun updateAddress(address: Address) : Address {
        if (address.id == null) {
            throw IllegalArgumentException("Can't update address with null id")
        }
        val originalAddress = findAddressById(address.id)
        return addressRepository.save(originalAddress.copy(
            country = address.country,
            state = address.state,
            zipCode = address.zipCode,
            city = address.city,
            streetAddress = address.streetAddress
        ))
    }

    fun findAllAdresses(): List<Address> {
        return addressRepository.findAll()
    }

}