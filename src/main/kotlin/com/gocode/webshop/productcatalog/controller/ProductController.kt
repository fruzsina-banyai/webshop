package com.gocode.webshop.productcatalog.controller

import com.gocode.webshop.productcatalog.dto.CategorizeProductDto
import com.gocode.webshop.productcatalog.dto.ProductDto
import com.gocode.webshop.productcatalog.dto.UpdateProductStockDto
import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.productcatalog.model.fromProductDto
import com.gocode.webshop.productcatalog.model.toProductDto
import com.gocode.webshop.productcatalog.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping
class ProductController(
    private val productService: ProductService
) {
    fun findProductById(productId: UUID): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.findProductById(productId).toProductDto())
    }

    fun createProduct(productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.createProduct(Product.fromProductDto(productDto)).toProductDto())
    }

    fun deactivateProduct(productId: UUID): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.deactivateProduct(productId).toProductDto())
    }

    fun activateProduct(productId: UUID): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.activateProduct(productId).toProductDto())
    }

    fun categorizeProduct(categorizeProductDto: CategorizeProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.categorizeProduct(categorizeProductDto.productId, categorizeProductDto.categoryId).toProductDto())
    }

    fun uncategorizeProduct(productId: UUID): ResponseEntity<Unit> {
        productService.uncategortizeProduct(productId)
        return ResponseEntity.ok().build()
    }

    fun updateProduct(productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.updateProduct(Product.fromProductDto(productDto)).toProductDto())
    }

    fun updateProductStock(updateProductStockDto: UpdateProductStockDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.updateProductStock(updateProductStockDto.productId, updateProductStockDto.stock).toProductDto())
    }

    fun findAllProducts(): ResponseEntity<List<ProductDto>> {
        return ResponseEntity
            .ok()
            .body(productService.findAllProducts().map { it.toProductDto() }.toList())
    }
}