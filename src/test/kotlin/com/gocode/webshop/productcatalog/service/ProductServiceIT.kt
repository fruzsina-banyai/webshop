package com.gocode.webshop.productcatalog.service

import com.gocode.webshop.productcatalog.model.Category
import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.errors.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

private const val ANY_NAME = "name"

private const val ANY_DESCRIPTION = "description"

private const val ANY_PRICE = 1.234

private const val ANY_IN_STOCK = 1.234

private const val UPDATED_NAME = "updatedName"

private const val UPDATED_DESCRIPTION = "updatedDescription"

private const val UPDATED_PRICE = 4.321

private const val UPDATED_IN_STOCK = 4.321

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceIT {
    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var categoryService: CategoryService

    @Test
    fun `should save new product with null id`() {
        val product = createProduct()

        val createdProduct = productService.createProduct(product.copy(id = null))
        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertNotNull(dbProduct.id)
        assertEquals(createdProduct.id, dbProduct.id)
    }

    @Test
    fun `should save new product with any id`() {
        val product = createProduct()

        val createdProduct = productService.createProduct(product)
        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertNotNull(dbProduct.id)
        assertEquals(createdProduct.id, dbProduct.id)
    }

    @Test
    fun `should throw error on non-existent category id when creating product`() {
        val product = createProduct()

        val category = createCategory()
        categoryService.createCategory(category)

        assertThrows<EntityNotFoundException> { productService.createProduct(product.copy(categoryId = category.id)) }
    }

    @Test
    fun `should activate product`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product.copy(active = false))

        productService.activateProduct(createdProduct.id!!)
        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertEquals(!createdProduct.active, dbProduct.active)
    }

    @Test
    fun `should deactivate product`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        productService.deactivateProduct(createdProduct.id!!)
        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertEquals(!createdProduct.active, dbProduct.active)
    }

    @Test
    fun `should throw error on non-existent category id when categorizing product`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        val category = createCategory()
        categoryService.createCategory(category)

        assertThrows<EntityNotFoundException> { productService.categorizeProduct(createdProduct.id!!, category.id!!) }
    }

    @Test
    fun `should categorize product with null category`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        val dbProduct = productService.categorizeProduct(createdProduct.id!!, createdCategory.id!!)

        assertEquals(createdCategory.id, dbProduct.categoryId)
    }

    @Test
    fun `should categorize product with existing category`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        val category1 = createCategory()
        val createdCategory1 = categoryService.createCategory(category1)

        productService.categorizeProduct(createdProduct.id!!, createdCategory1.id!!)

        val category2 = createCategory()
        val createdCategory2 = categoryService.createCategory(category2)

        val dbProduct = productService.categorizeProduct(createdProduct.id!!, createdCategory2.id!!)

        assertEquals(createdCategory2.id, dbProduct.categoryId)
    }

    @Test
    fun `should uncategorize product with null category id given product id when categorizing product`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        productService.uncategorizeProduct(createdProduct.id!!)

        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertNull(dbProduct.categoryId)
    }

    @Test
    fun `should uncategorize product with any category id given product id`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        productService.categorizeProduct(createdProduct.id!!, createdCategory.id!!)

        val dbProduct = productService.uncategorizeProduct(createdProduct.id!!)

        assertNull(dbProduct.categoryId)
    }

    @Test
    fun `should uncategorize product with null category id`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        productService.uncategorizeProduct(createdProduct)

        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertNull(dbProduct.categoryId)
    }

    @Test
    fun `should uncategorize product with any category id`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        val category = createCategory()
        val createdCategory = categoryService.createCategory(category)

        productService.categorizeProduct(createdProduct.id!!, createdCategory.id!!)

        val dbProduct = productService.uncategorizeProduct(createdProduct)

        assertNull(dbProduct.categoryId)
    }

    @Test
    fun `should throw error on non-existent id`() {
        val product = createProduct()

        productService.createProduct(product)

        assertThrows<EntityNotFoundException> { productService.updateProduct(product) }
    }

    @Test
    fun `should make a new instance of entity on update`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)
        val updatedProduct = createProduct().copy(
            id = createdProduct.id,
            categoryId = UUID.randomUUID(),
            name = UPDATED_NAME,
            active = true,
            description = UPDATED_DESCRIPTION,
            price = UPDATED_PRICE,
            inStock = UPDATED_IN_STOCK
        )

        val updatedDbProduct = productService.updateProduct(updatedProduct)
        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertNotNull(updatedDbProduct.id)
        assertNotEquals(updatedDbProduct.id, dbProduct.id)
    }

    @Test
    fun `should only save updatable fields`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)
        val updatedProduct = createProduct().copy(
            id = createdProduct.id,
            categoryId = UUID.randomUUID(),
            name = UPDATED_NAME,
            active = true,
            description = UPDATED_DESCRIPTION,
            price = UPDATED_PRICE,
            inStock = UPDATED_IN_STOCK
        )

        val updatedDbProduct = productService.updateProduct(updatedProduct)
        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertEquals(createdProduct.categoryId, updatedDbProduct.categoryId)
        assertEquals(updatedProduct.name, updatedDbProduct.name)
        assertTrue(updatedDbProduct.active)
        assertFalse(dbProduct.active)
        assertEquals(updatedProduct.description, updatedDbProduct.description)
        assertEquals(updatedProduct.price, updatedDbProduct.price)
        assertEquals(createdProduct.inStock, updatedDbProduct.inStock)
    }

    @Test
    fun `should update stock correctly`() {
        val product = createProduct()
        val createdProduct = productService.createProduct(product)

        productService.updateProductStock(createdProduct.id!!, UPDATED_IN_STOCK)

        val dbProduct = productService.findProductById(createdProduct.id!!)

        assertEquals(UPDATED_IN_STOCK, dbProduct.inStock)
    }

    companion object {
        fun createProduct(): Product {
            return Product(
                id = UUID.randomUUID(),
                categoryId = null,
                name = ANY_NAME,
                description = ANY_DESCRIPTION,
                price = ANY_PRICE,
                inStock = ANY_IN_STOCK,
            )
        }

        fun createCategory(): Category {
            return Category(
                id = UUID.randomUUID(),
                name = ANY_NAME,
                description = ANY_DESCRIPTION,
                parentId = null,
            )
        }
    }
}