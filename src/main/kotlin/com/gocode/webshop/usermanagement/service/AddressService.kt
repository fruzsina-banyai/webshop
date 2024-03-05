package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.repository.AddressRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.UUID

private const val DELETED = "deleted"

@Service
class AddressService (
    private val addressRepository : AddressRepository
) {
    fun findAddressById(id: UUID) : Address {
        return addressRepository.findById(id).orElseThrow { throw EntityNotFoundException(id.toString(), Address::class.java) }
    }

    fun createAddress(address: Address) : Address {
        return addressRepository.save(address.copy(id=null))
    }

    fun deleteAddress(id: UUID) : Address {
        val address = findAddressById(id)
        return addressRepository.save(address.copy(
            streetAddress = DELETED + id,
            deleted = true,
        ))
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

    fun findAllAddresses(): List<Address> {
        return addressRepository.findAll()
    }

}