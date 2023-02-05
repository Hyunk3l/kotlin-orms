package com.fabridinapoli.kotlinorms.infrastructure.configuration

import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerService
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.postgres.PostgresCustomerRepository
import java.time.Clock
import javax.sql.DataSource
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.jooq.tools.LoggerListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

@Configuration
class ApplicationConfiguration {

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean
    fun connectionProvider() = DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))

    fun configuration() = DefaultConfiguration().also {
        it.set(connectionProvider())
        it.set(DefaultExecuteListenerProvider(LoggerListener()))
    }

    @Bean
    fun dsl() = DefaultDSLContext(configuration())

    @Bean
    fun customerRepository(jdbcTemplate: JdbcTemplate) = PostgresCustomerRepository(jdbcTemplate, Clock.systemUTC())

    @Bean
    fun signUpCustomerService(repository: CustomerRepository) = SignUpCustomerService(repository)
}
