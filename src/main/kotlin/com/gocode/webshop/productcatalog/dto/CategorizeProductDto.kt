package com.gocode.webshop.productcatalog.dto

import java.util.UUID

data class CategorizeProductDto(
    val productId: UUID,
    val categoryId: UUID
)
