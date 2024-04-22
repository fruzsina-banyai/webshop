package com.gocode.webshop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebshopApplication {
	/*@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}*/
}

fun main(args: Array<String>) {
	runApplication<WebshopApplication>(*args)
}
