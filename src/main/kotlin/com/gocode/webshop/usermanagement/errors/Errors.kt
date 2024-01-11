package com.gocode.webshop.usermanagement.errors

import org.springframework.http.HttpStatus

abstract class WebshopException(
    val objectId: String?,
    val objectClass: Class<*>?,
    val responseCode: HttpStatus,
    message: String
) : Exception (message)


class EntityNotFoundException(
    objectId: String,
    objectClass: Class<*>
) : WebshopException(
    objectId = objectId,
    objectClass = objectClass,
    responseCode = HttpStatus.NOT_FOUND,
    message = "Entity not found!"
)