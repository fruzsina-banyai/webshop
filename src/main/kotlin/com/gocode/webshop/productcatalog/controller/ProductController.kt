package com.gocode.webshop.productcatalog.controller

import com.gocode.webshop.productcatalog.dto.CategorizeProductDto
import com.gocode.webshop.productcatalog.dto.ProductDto
import com.gocode.webshop.productcatalog.dto.UpdateProductStockDto
import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.productcatalog.model.fromProductDto
import com.gocode.webshop.productcatalog.model.toProductDto
import com.gocode.webshop.productcatalog.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/product")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping("/{productId}")
    fun findProductById(@PathVariable productId: UUID): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.findProductById(productId).toProductDto())
    }

    @GetMapping("/by-category/{categoryId}")
    fun findByCategoryId(@PathVariable categoryId: UUID): ResponseEntity<List<ProductDto>> {
        return ResponseEntity
            .ok()
            .body(productService.findByCategoryId(categoryId).map { it.toProductDto() })
    }

    @PostMapping
    fun createProduct(@RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.createProduct(Product.fromProductDto(productDto)).toProductDto())
    }

    @PutMapping("/{productId}/deactivate")
    fun deactivateProduct(@PathVariable productId: UUID): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.deactivateProduct(productId).toProductDto())
    }

    @PutMapping("/{productId}/activate")
    fun activateProduct(@PathVariable productId: UUID): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.activateProduct(productId).toProductDto())
    }

    @PutMapping("/categorize")
    fun categorizeProduct(@RequestBody categorizeProductDto: CategorizeProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.categorizeProduct(categorizeProductDto.productId, categorizeProductDto.categoryId).toProductDto())
    }

    @PutMapping("/{productId}/uncategorize")
    fun uncategorizeProduct(@PathVariable productId: UUID): ResponseEntity<ProductDto> {
        productService.uncategortizeProduct(productId)
        return ResponseEntity.ok().build()
    }

    @PutMapping
    fun updateProduct(@RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.updateProduct(Product.fromProductDto(productDto)).toProductDto())
    }

    @PutMapping("/update-stock")
    fun updateProductStock(@RequestBody updateProductStockDto: UpdateProductStockDto): ResponseEntity<ProductDto> {
        return ResponseEntity
            .ok()
            .body(productService.updateProductStock(updateProductStockDto.productId, updateProductStockDto.stock).toProductDto())
    }

    @GetMapping
    fun findAllProducts(): ResponseEntity<List<ProductDto>> {
        return ResponseEntity
            .ok()
            .body(productService.findAllProducts().map { it.toProductDto() }.toList())
    }
}