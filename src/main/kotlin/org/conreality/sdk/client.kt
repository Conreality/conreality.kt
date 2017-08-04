@file:JvmName("SDK")
@file:JvmMultifileClass

package org.conreality.sdk

import java.sql.Connection
import java.sql.DriverManager

import com.zaxxer.hikari.HikariDataSource

class Client(dbName: String) {
  private val connectionPool = HikariDataSource()

  //val isClosed get() = connection.isClosed // FIXME

  init {
    Class.forName("org.postgresql.Driver").newInstance()
    connectionPool.setJdbcUrl("jdbc:postgresql:" + dbName)
  }

  fun execute(sqlCommand: String) {
    val connection = connectionPool.getConnection("", "")
    val statement = connection.createStatement()
    statement.execute(sqlCommand)
  }

  fun login() {} // TODO
}
