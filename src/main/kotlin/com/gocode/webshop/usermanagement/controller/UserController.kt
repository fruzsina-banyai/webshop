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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping
class UserController(
    private val userService: UserService
) {
    fun findUserById(userId: UUID): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.findUserById(userId).toUserDto())
    }

    fun createUser(userDto: UserDto): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.createUser(User.fromUserDto(userDto)).toUserDto())
    }

    fun deleteUser(userId: UUID): ResponseEntity<Unit> {
        userService.deleteUser(userId)
        return ResponseEntity.ok().build()
    }

    fun updateUser(userDto: UserDto): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.updateUser(User.fromUserDto(userDto)).toUserDto())
    }

    fun changePassword(passwordChangeDto: PasswordChangeDto): ResponseEntity<UserDto> {
        return ResponseEntity
            .ok()
            .body(userService.changePassword(passwordChangeDto.userId, passwordChangeDto.password).toUserDto())
    }

    fun getAddresses(userId: UUID): ResponseEntity<List<AddressDto>> {
        return ResponseEntity
            .ok()
            .body(userService.getAddresses(userId).map { it.toAddressDto() }.toList())
    }

    fun deleteAddresses(userId: UUID): ResponseEntity<Unit> {
        userService.deleteAddresses(userId)
        return ResponseEntity.ok().build()
    }

    fun findAllUsers(): ResponseEntity<List<UserDto>> {
        return ResponseEntity
            .ok()
            .body(userService.findAllUsers().map { it.toUserDto() }.toList())
    }
}