package com.albertattard.presentation.contacts

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseFactory {

    @Bean
    fun database(dataSource: DataSource): Database =
        Database.connect(dataSource).apply {
            transaction(this) {
                SchemaUtils.createMissingTablesAndColumns(ContactsTable)
            }
        }

    @Bean
    fun dataSource(): DataSource =
        HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:contacts;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE"
            driverClassName = "org.h2.Driver"
            username = "demo"
            password = "demo"
        }.let {
            HikariDataSource(it)
        }
}
