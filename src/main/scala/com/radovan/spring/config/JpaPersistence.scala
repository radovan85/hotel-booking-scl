package com.radovan.spring.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.{JpaTransactionManager, JpaVendorAdapter, LocalContainerEntityManagerFactoryBean}
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import java.util.Properties

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = Array("com.radovan.spring.repository"))
class JpaPersistence {

  @Bean
  def entityManagerFactory: LocalContainerEntityManagerFactoryBean = {
    val em = new LocalContainerEntityManagerFactoryBean
    em.setDataSource(getHikariDataSource)
    em.setPackagesToScan("com.radovan.spring")
    val vendorAdapter:JpaVendorAdapter = new HibernateJpaVendorAdapter
    em.setJpaVendorAdapter(vendorAdapter)
    em.setJpaProperties(additionalProperties)
    em
  }

  @Bean
  def getHikariDataSource: HikariDataSource = {
    val returnValue = new HikariDataSource
    returnValue.setDriverClassName("org.postgresql.Driver")
    returnValue.setJdbcUrl("jdbc:postgresql://localhost:5432/hoteldb")
    returnValue.setUsername("postgres")
    returnValue.setPassword("1111")
    returnValue
  }

  @Bean
  def transactionManager: PlatformTransactionManager = {
    val transactionManager = new JpaTransactionManager
    transactionManager.setEntityManagerFactory(entityManagerFactory.getObject)
    transactionManager
  }

  @Bean
  def exceptionTranslation = new PersistenceExceptionTranslationPostProcessor

  private[config] def additionalProperties = {
    val properties = new Properties
    properties.setProperty("hibernate.hbm2ddl.auto", "update")
    properties
  }
}

