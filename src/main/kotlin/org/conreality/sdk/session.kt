package org.conreality.sdk

import java.sql.Connection
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong

import com.zaxxer.hikari.HikariDataSource

private val ID = AtomicLong()

/**
 * An authenticated session with a Conreality master.
 *
 * @constructor
 * @property client
 * @property agentUUID
 * @property password
 */
class Session(val client: Client, val agentUUID: UUID, password: String = "") {
  val id = ID.incrementAndGet()

  private val connectionPool = HikariDataSource()

  val isClosed get() = connectionPool.isClosed

  /**
   * The game this session belongs to.
   */
  val game = Game(this)

  init {
    connectionPool.setPoolName("org.conreality.sdk.Session:" + id)
    connectionPool.setJdbcUrl(client.connectionURL)
    connectionPool.setUsername(agentUUID.toString())
    connectionPool.setPassword(password)
  }

  /**
   * @suppress
   */
  fun execute(sqlCommand: String) {
    val connection = connectionPool.getConnection()
    val statement = connection.createStatement()
    statement.execute(sqlCommand)
  }
}
