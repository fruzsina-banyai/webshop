package com.gocode.webshop.usermanagement.controller

import com.gocode.webshop.usermanagement.dto.AddressDto
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.fromAddressDto
import com.gocode.webshop.usermanagement.model.toAddressDto
import com.gocode.webshop.usermanagement.service.AddressService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping
class AddressController(
    private val addressService: AddressService
) {
    fun findAddressById(addressId: UUID): ResponseEntity<AddressDto> {
        return ResponseEntity
            .ok()
            .body(addressService.findAddressById(addressId).toAddressDto())
    }

    fun createAddress(addressDto: AddressDto): ResponseEntity<AddressDto> {
        return ResponseEntity
            .ok()
            .body(addressService.createAddress(Address.fromAddressDto(addressDto)).toAddressDto())
    }

    fun deleteAddress(id: UUID): ResponseEntity<Unit> {
        addressService.deleteAddress(id)
        return ResponseEntity.ok().build()
    }

    fun updateAddress(addressDto: AddressDto): ResponseEntity<AddressDto> {
        return ResponseEntity
            .ok()
            .body(addressService.updateAddress(Address.fromAddressDto(addressDto)).toAddressDto())
    }

    fun findAllAddresses(): ResponseEntity<List<AddressDto>> {
        return ResponseEntity
            .ok()
            .body(addressService.findAllAdresses().map { it.toAddressDto() }.toList())
    }
}