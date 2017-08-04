@file:JvmName("SDK")
@file:JvmMultifileClass

package org.conreality.sdk

import java.sql.Connection
import java.sql.DriverManager
import java.util.UUID

class Client(val gameID: String) {
  val connectionURL: String

  init {
    Class.forName("org.postgresql.Driver").newInstance()
    connectionURL = "jdbc:postgresql:" + gameID
  }

  fun login(): Session {
    return login("public")
  }

  fun login(agentID: String, password: String = ""): Session {
    val agentUUID = UUID.fromString(agentID) // TODO: support names
    return login(agentUUID, password)
  }

  fun login(agentUUID: UUID, password: String = ""): Session {
    return Session(this, agentUUID, password)
  }
}
