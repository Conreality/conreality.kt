@file:JvmName("SDK")
@file:JvmMultifileClass

package org.conreality

import java.sql.Connection
import java.sql.DriverManager
import org.postgresql.jdbc.*

class Client(dbName: String) {
  var connection: Connection

  init {
    Class.forName("org.postgresql.Driver").newInstance()

    connection = DriverManager.getConnection("jdbc:postgresql:" + dbName)
  }
}
