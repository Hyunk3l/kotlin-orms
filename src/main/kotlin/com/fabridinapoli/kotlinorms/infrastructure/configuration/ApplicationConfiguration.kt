package com.fabridinapoli.kotlinorms.infrastructure.configuration

import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerService
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.postgres.PostgresCustomerRepository
import java.time.Clock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class ApplicationConfiguration {

    @Bean
    fun customerRepository(jdbcTemplate: JdbcTemplate) = PostgresCustomerRepository(jdbcTemplate, Clock.systemUTC())

    @Bean
    fun signUpCustomerService(repository: CustomerRepository) = SignUpCustomerService(repository)
}
