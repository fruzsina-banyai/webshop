package com.gocode.webshop.productcatalog.dto

import java.util.UUID

data class ProductDto(
    val id: UUID?,
    val categoryId: UUID?,
    val name: String,
    val active: Boolean,
    val description: String,
    val price: Double,
    val inStock: Double
)
