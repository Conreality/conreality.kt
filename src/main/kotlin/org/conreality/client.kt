@file:JvmName("SDK")
@file:JvmMultifileClass

package org.conreality

import java.sql.Connection
import java.sql.DriverManager
import org.postgresql.jdbc.*

class Client(dbName: String) {
  private val connection: Connection

  val isClosed get() = connection.isClosed

  init {
    Class.forName("org.postgresql.Driver").newInstance()

    connection = DriverManager.getConnection("jdbc:postgresql:" + dbName)
  }

  fun execute(sqlCommand: String) {
    val statement = connection.createStatement()
    statement.execute(sqlCommand)
  }
}
