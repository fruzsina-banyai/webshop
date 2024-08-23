package com.gocode.webshop.usermanagement.controller

import com.gocode.webshop.usermanagement.dto.AddressDto
import com.gocode.webshop.usermanagement.model.Address
import com.gocode.webshop.usermanagement.model.fromAddressDto
import com.gocode.webshop.usermanagement.model.toAddressDto
import com.gocode.webshop.usermanagement.service.AddressService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/address")
class AddressController(
    private val addressService: AddressService
) {
    @GetMapping("/{addressId}")
    fun findAddressById(@PathVariable addressId: UUID): ResponseEntity<AddressDto> {
        return ResponseEntity
            .ok()
            .body(addressService.findAddressById(addressId).toAddressDto())
    }

    @GetMapping("/by-user/{userId}")
    fun findByUserId(@PathVariable userId: UUID): ResponseEntity<List<AddressDto>> {
        return ResponseEntity
            .ok()
            .body(addressService.findByUserId(userId).map { it.toAddressDto() })
    }

    @PostMapping
    fun createAddress(@RequestBody addressDto: AddressDto): ResponseEntity<AddressDto> {
        return ResponseEntity
            .ok()
            .body(addressService.createAddress(Address.fromAddressDto(addressDto)).toAddressDto())
    }

    @DeleteMapping("/{addressId}")
    fun deleteAddress(@PathVariable addressId: UUID): ResponseEntity<Unit> {
        return ResponseEntity
            .ok()
            .body(addressService.deleteAddress(addressId))
    }

    @PutMapping
    fun updateAddress(@RequestBody addressDto: AddressDto): ResponseEntity<AddressDto> {
        return ResponseEntity
            .ok()
            .body(addressService.updateAddress(Address.fromAddressDto(addressDto)).toAddressDto())
    }

    @GetMapping
    fun findAllAddresses(): ResponseEntity<List<AddressDto>> {
        return ResponseEntity
            .ok()
            .body(addressService.findAllAddresses().map { it.toAddressDto() }.toList())
    }

    @GetMapping("/non-deleted")
    fun findAllNonDeletedAddresses(): ResponseEntity<List<AddressDto>> {
        return ResponseEntity
            .ok()
            .body(addressService.findAllNonDeletedAddresses().map { it.toAddressDto() }.toList())
    }
}