package com.gocode.webshop.usermanagement.model

import com.gocode.webshop.usermanagement.dto.UserDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.UUID

@Entity
@Table(name = "user")
data class User (
    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    val id: UUID?,

    @Column(name = "role", nullable = false)
    val role: String,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    @Column(name = "phone_number", nullable = false)
    val phoneNumber: String,

    @Column(name = "password", nullable = false)
    val password: String
) {
    companion object;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "User(id=$id, role='$role', firstName='$firstName', lastName='$lastName', email='$email', phoneNumber='$phoneNumber', password='$password')"
    }
}

fun User.Companion.fromUserDto(userDto: UserDto) : User {
    return User(
        id = userDto.id,
        role = userDto.role,
        firstName = userDto.firstName,
        lastName = userDto.lastName,
        email = userDto.email,
        phoneNumber = userDto.phoneNumber,
        password = "")
}

fun User.toUserDto() : UserDto = UserDto (
    id = id,
    role = role,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phoneNumber = phoneNumber,
)