package com.fabridinapoli.kotlinorms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@SpringBootApplication
class Application

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
