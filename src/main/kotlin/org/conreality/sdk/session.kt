@file:JvmName("SDK")
@file:JvmMultifileClass

package org.conreality.sdk

import java.sql.Connection
import java.util.UUID

class Session(val connection: Connection, val agentUUID: UUID) {
  val isClosed get() = connection.isClosed
}
