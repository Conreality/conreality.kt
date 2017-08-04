package org.conreality.sdk

import java.sql.Connection
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

import com.zaxxer.hikari.HikariDataSource

private val ID = AtomicLong()

class Session(val client: Client, val agentUUID: UUID, password: String = "") {
  val id = ID.incrementAndGet()
  private val connectionPool = HikariDataSource()

  val isClosed get() = connectionPool.isClosed

  init {
    connectionPool.setPoolName("org.conreality.sdk.Session:" + id)
    connectionPool.setJdbcUrl(client.connectionURL)
    connectionPool.setUsername(agentUUID.toString())
    connectionPool.setPassword(password)
  }

  fun execute(sqlCommand: String) {
    val connection = connectionPool.getConnection()
    val statement = connection.createStatement()
    statement.execute(sqlCommand)
  }
}
