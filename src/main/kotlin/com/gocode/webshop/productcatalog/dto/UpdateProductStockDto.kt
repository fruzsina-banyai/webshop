package com.gocode.webshop.productcatalog.dto

import java.util.UUID

data class UpdateProductStockDto(
    val productId: UUID,
    val stock: Double
)
