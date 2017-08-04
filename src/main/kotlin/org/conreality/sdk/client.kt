/* This is free and unencumbered software released into the public domain. */

package org.conreality.sdk

import java.sql.Connection
import java.sql.DriverManager
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

/**
 * An unauthenticated connection to a Conreality master.
 *
 * @constructor Creates a client connection
 * @property gameURL a URL string in the form "tcp://localhost:1234/game"
 */
class Client(val gameURL: String) : AutoCloseable {
  private var isClosed = AtomicBoolean(false)

  /**
   * @see https://jdbc.postgresql.org/documentation/94/connect.html
   */
  val connectionURL: String

  init {
    Class.forName("org.postgresql.Driver").newInstance()

    val gameID = gameURL // TODO: parse the URL
    connectionURL = "jdbc:postgresql:" + gameID
  }

  /**
   * Closes this client connection.
   *
   * Does not affect any previously-established sessions, only prevents the
   * creation of new sessions.
   *
   * This method is idempotent.
   */
  @Throws(Exception::class)
  override fun close() {
    isClosed.compareAndSet(false, true)
  }

  fun login(): Session {
    return login("public")
  }

  fun login(agentID: String, password: String = ""): Session {
    val agentUUID = UUID.fromString(agentID) // TODO: support names
    return login(agentUUID, password)
  }

  fun login(agentUUID: UUID, password: String = ""): Session {
    // TODO: throw exception if isClosed == true
    return Session(this, agentUUID, password)
  }
}
