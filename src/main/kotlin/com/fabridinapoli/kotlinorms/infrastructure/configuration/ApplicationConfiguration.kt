package com.fabridinapoli.kotlinorms.infrastructure.configuration

import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerService
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.jdbctemplate.JdbcTemplateCustomerRepository
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.ktorm.KtormCustomerRepository
import java.time.Clock
import javax.sql.DataSource
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.jooq.tools.LoggerListener
import org.ktorm.database.Database
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
        it.set(SQLDialect.POSTGRES)
        it.set(DefaultExecuteListenerProvider(LoggerListener()))
    }

    @Bean
    fun clock(): Clock = Clock.systemUTC()

    @Bean
    fun dsl() = DefaultDSLContext(configuration())

    @Bean
    fun ktormCustomerRepository(clock: Clock): KtormCustomerRepository {
        val database = Database.connectWithSpringSupport(dataSource = dataSource)
        return KtormCustomerRepository(
            database = database,
            clock = clock
        )
    }

    @Bean
    fun customerRepository(jdbcTemplate: JdbcTemplate) = JdbcTemplateCustomerRepository(jdbcTemplate, Clock.systemUTC())

    @Bean
    fun signUpCustomerService(customerRepository: JdbcTemplateCustomerRepository) =
        SignUpCustomerService(customerRepository)
}
