package com.gocode.webshop.productcatalog.service

import com.gocode.webshop.productcatalog.model.Category
import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.productcatalog.repository.ProductRepository
import com.gocode.webshop.errors.EntityNotFoundException
import com.gocode.webshop.productcatalog.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {
    fun findProductById(productId: UUID): Product {
        return productRepository.findById(productId)
            .orElseThrow { throw EntityNotFoundException(productId.toString(), Product::class.java) }
    }

    fun findByCategoryId(categoryId: UUID): List<Product> {
        return productRepository.findByCategoryId(categoryId)
    }

    fun createProduct(product: Product): Product {
        return productRepository.save(product.copy(id = null))
    }

    fun deactivateProduct(productId: UUID): Product {
        val product = findProductById(productId)
        return productRepository.save(product.copy(active = false))
    }

    fun activateProduct(productId: UUID): Product {
        val product = findProductById(productId)
        return productRepository.save(product.copy(active = true))
    }

    fun categorizeProduct(productId: UUID, categoryId: UUID): Product {
        val product = findProductById(productId)
        if (categoryRepository.existsById(categoryId)) {
            return productRepository.save(product.copy(categoryId = categoryId))
        } else {
            throw EntityNotFoundException(categoryId.toString(), Category::class.java)
        }
    }

    fun uncategortizeProduct(productId: UUID): Product {
        val product = findProductById(productId)
        return productRepository.save(product.copy(categoryId = null))
    }

    fun uncategortizeProduct(product: Product): Product {
        return productRepository.save(product.copy(categoryId = null))
    }

    fun updateProduct(product: Product): Product {
        if (product.id == null) {
            throw IllegalArgumentException("Can't update product with null id!")
        }
        val originalProduct = findProductById(product.id)
        deactivateProduct(originalProduct.id!!)
        return productRepository.save(
            originalProduct.copy(
                id = null,
                name = product.name,
                active = true,
                description = product.description,
                price = product.price,
            )
        )
    }

    fun updateProductStock(productId: UUID, stock: Double) : Product {
        val product = findProductById(productId)
        return productRepository.save(product.copy(inStock = stock))
    }

    fun findAllProducts(): List<Product> {
        return productRepository.findAll()
    }
}