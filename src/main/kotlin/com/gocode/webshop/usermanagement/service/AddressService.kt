package com.gocode.webshop.usermanagement.service

import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.constants.DELETED
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.repository.AddressRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.UUID

@Service
class AddressService (
    private val addressRepository : AddressRepository
) {
    fun findAddressById(addressId: UUID) : Address {
        return addressRepository.findById(addressId).orElseThrow { throw EntityNotFoundException(addressId.toString(), Address::class.java) }
    }

    fun findByUserId(userId: UUID) : List<Address> {
        return addressRepository.findByUserId(userId)
    }

    fun createAddress(address: Address) : Address {
        return addressRepository.save(address.copy(id=null))
    }

    fun deleteAddress(addressId: UUID) : Address {
        val address = findAddressById(addressId)
        return addressRepository.save(address.copy(
            streetAddress = DELETED + addressId,
            deleted = true,
        ))
    }

    fun deleteAddress(address: Address) : Address {
        return addressRepository.save(address.copy(
            streetAddress = DELETED + address.id,
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