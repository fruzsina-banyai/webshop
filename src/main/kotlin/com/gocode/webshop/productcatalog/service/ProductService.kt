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
        if (!categoryRepository.existsById(categoryId)) {
            throw EntityNotFoundException(categoryId.toString(), Category::class.java)
        }
        return productRepository.findByCategoryId(categoryId)
    }

//    fun findProductsByIdList(productIds: List<UUID>): List<Product> {
//        return productIds.map { findProductById(it) }
//    }

    fun createProduct(product: Product): Product {
        if (product.categoryId != null && !categoryRepository.existsById(product.categoryId)){
            throw EntityNotFoundException(product.categoryId.toString(), Category::class.java)
        }
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
        if (!categoryRepository.existsById(categoryId)) {
            throw EntityNotFoundException(categoryId.toString(), Category::class.java)
        }
        return productRepository.save(product.copy(categoryId = categoryId))
    }

    fun uncategorizeProduct(productId: UUID): Product {
        val product = findProductById(productId)
        return productRepository.save(product.copy(categoryId = null))
    }

    fun uncategorizeProduct(product: Product): Product {
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

    fun updateProductStock(productId: UUID, stock: Double): Product {
        val product = findProductById(productId)
        return productRepository.save(product.copy(inStock = stock))
    }

    fun findAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    fun findAllActiveProducts(): List<Product> {
        return findAllProducts().filter { it.active }
    }
}