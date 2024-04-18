package com.gocode.webshop.usermanagement.controller

import com.gocode.webshop.usermanagement.dto.AddressDto
import com.gocode.webshop.usermanagement.dto.PasswordChangeDto
import com.gocode.webshop.usermanagement.dto.UserDto
import com.gocode.webshop.usermanagement.model.User
import com.gocode.webshop.usermanagement.model.fromUserDto
import com.gocode.webshop.usermanagement.model.toAddressDto
import com.gocode.webshop.usermanagement.model.toUserDto
import com.gocode.webshop.usermanagement.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/{userId}")
    fun findUserById(@PathVariable userId: UUID): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.findUserById(userId).toUserDto())
    }

    @PostMapping
    fun createUser(userDto: UserDto): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.createUser(User.fromUserDto(userDto)).toUserDto())
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: UUID): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.deleteUser(userId).toUserDto())
    }

    @PutMapping
    fun updateUser(userDto: UserDto): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.updateUser(User.fromUserDto(userDto)).toUserDto())
    }

    @PutMapping("/change-password")
    fun changePassword(passwordChangeDto: PasswordChangeDto): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.changePassword(passwordChangeDto.userId, passwordChangeDto.password).toUserDto())
    }

    @GetMapping("/{userId}/get-addresses")
    fun getAddresses(@PathVariable userId: UUID): ResponseEntity<List<AddressDto>> {
        return ResponseEntity
            .ok()
            .body(userService.getAddresses(userId).map { it.toAddressDto() }.toList())
    }

    @DeleteMapping("/{userId}/delete-addresses")
    fun deleteAddresses(@PathVariable userId: UUID): ResponseEntity<Unit> {
        userService.deleteAddresses(userId)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun findAllUsers(): ResponseEntity<List<UserDto>> {
        return ResponseEntity
            .ok()
            .body(userService.findAllUsers().map { it.toUserDto() }.toList())
    }
}