package com.gocode.webshop.productcatalog.model

import com.gocode.webshop.productcatalog.dto.ProductDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    val id: UUID?,

    @Column(name = "category_id", nullable = true)
    val categoryId: UUID?,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "active", nullable = false)
    val active: Boolean = true,

    @Column(name = "description", nullable = true)
    val description: String,

    @Column(name = "price", nullable = false)
    val price: Double,

    @Column(name = "in_stock", nullable = false)
    val inStock: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Product

        return id == other.id
    }

    companion object;

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Product(id=$id, categoryId=$categoryId, name='$name', description='$description', price=$price, inStock=$inStock)"
    }
}

fun Product.Companion.fromProductDto(productDto: ProductDto): Product {
    return Product(
        id = productDto.id,
        categoryId = productDto.categoryId,
        name = productDto.name,
        active = productDto.active,
        description = productDto.description,
        price = productDto.price,
        inStock = productDto.inStock
    )
}

fun Product.toProductDto(): ProductDto {
    return ProductDto(
        id = id,
        categoryId = categoryId,
        name = name,
        active = active,
        description = description,
        price = price,
        inStock = inStock
    )
}