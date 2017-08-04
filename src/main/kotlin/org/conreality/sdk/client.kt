@file:JvmName("SDK")
@file:JvmMultifileClass

package org.conreality.sdk

import java.util.UUID
import java.sql.Connection
import java.sql.DriverManager

import com.zaxxer.hikari.HikariDataSource

class Client(gameID: String) {
  private val connectionPool = HikariDataSource()

  init {
    Class.forName("org.postgresql.Driver").newInstance()
    connectionPool.setJdbcUrl("jdbc:postgresql:" + gameID)
  }

  fun login(agentID: String, password: String = ""): Session {
    val agentUUID = UUID.fromString(agentID) // TODO: support names
    return login(agentUUID, password)
  }

  fun login(agentUUID: UUID, password: String = ""): Session {
    val connection = connectionPool.getConnection(agentUUID.toString(), password)
    return Session(connection, agentUUID)
  }

  fun execute(sqlCommand: String) {
    val connection = connectionPool.getConnection()
    val statement = connection.createStatement()
    statement.execute(sqlCommand)
  }
}
