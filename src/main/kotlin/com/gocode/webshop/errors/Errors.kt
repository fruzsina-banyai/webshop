package com.gocode.webshop.errors

import com.gocode.webshop.productcatalog.model.Product
import com.gocode.webshop.usermanagement.model.User
import org.springframework.http.HttpStatus

abstract class WebshopException(
    val objectId: String?,
    val objectClass: Class<*>?,
    val responseCode: HttpStatus,
    message: String
) : Exception(message)


class EntityNotFoundException(
    objectId: String,
    objectClass: Class<*>
) : WebshopException(
    objectId = objectId,
    objectClass = objectClass,
    responseCode = HttpStatus.NOT_FOUND,
    message = "Entity not found!"
)

class NoAvailableStockException(
    objectId: String,
) : WebshopException(
    objectId = objectId,
    objectClass = Product::class.java,
    responseCode = HttpStatus.CONFLICT,
    message = "Stock not available!"
)

class EmailAlreadyInUseException(
    objectId: String,
) : WebshopException(
    objectId = objectId,
    objectClass = User::class.java,
    responseCode = HttpStatus.CONFLICT,
    message = "User already exists with this email"
)

class UserNotLoggedInException : WebshopException(
    objectId = null,
    objectClass = null,
    responseCode = HttpStatus.BAD_REQUEST,
    message = "User not logged in!"
)