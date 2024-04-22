package com.gocode.webshop.usermanagement.model

import com.gocode.webshop.usermanagement.dto.AddressDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.UUID

@Entity
@Table(name = "address")
data class Address (
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    val id: UUID?,

    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    val userId: UUID,

    @Column(name = "deleted", nullable = false)
    val deleted: Boolean = false,

    @Column(name = "country", nullable = false)
    val country: String,

    @Column(name = "state", nullable = false)
    val state: String,

    @Column(name = "zip_code", nullable = false)
    val zipCode: String,

    @Column(name = "city", nullable = false)
    val city: String,

    @Column(name = "street_address", nullable = false)
    val streetAddress: String,
) {
    companion object;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Address

        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Address(id=$id, country='$country', state='$state', zipCode='$zipCode', city='$city', streetAddress='$streetAddress')"
    }
}

fun Address.Companion.fromAddressDto(addressDto: AddressDto) : Address {
    return Address(
        id = addressDto.id,
        userId = addressDto.userId,
        deleted = addressDto.deleted,
        country = addressDto.country,
        state = addressDto.state,
        zipCode = addressDto.zipCode,
        city = addressDto.city,
        streetAddress = addressDto.streetAddress,
    )
}

fun Address.toAddressDto() : AddressDto = AddressDto(
    id = id,
    userId = userId,
    deleted = deleted,
    country = country,
    state = state,
    zipCode = zipCode,
    city = city,
    streetAddress = streetAddress
)