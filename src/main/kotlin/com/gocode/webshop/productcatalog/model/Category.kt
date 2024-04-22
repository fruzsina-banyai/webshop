package com.gocode.webshop.productcatalog.model

import com.gocode.webshop.productcatalog.dto.CategoryDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table(name = "category")
data class Category(
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    val id: UUID?,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "active", nullable = false)
    val active: Boolean = true,

    @Column(name = "description", nullable = true)
    val description: String,

    @Column(name = "parent_id", nullable = true)
    val parentId: UUID?
) {
    companion object;

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Product

        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Category(id=$id, name='$name', description='$description', parentId=$parentId)"
    }
}

fun Category.Companion.fromCategoryDto(categoryDto: CategoryDto) : Category {
    return Category(
        id = categoryDto.id,
        name = categoryDto.name,
        active = categoryDto.active,
        description = categoryDto.description,
        parentId = categoryDto.parentId
    )
}

fun Category.toCategoryDto() : CategoryDto{
    return CategoryDto(
        id = id,
        name = name,
        active = active,
        description = description,
        parentId = parentId
    )
}