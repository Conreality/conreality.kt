package org.conreality.sdk

import java.sql.SQLException
import java.sql.Types

/**
 * A transactional action to mutate Conreality master state.
 *
 * @constructor
 * @property session the current session
 */
class Action(val session: Session) : AutoCloseable {
  private val connection = session.getConnection()

  val isPending get() = !isClosed

  val isClosed get() = connection.isClosed

  /**
   * This method is idempotent.
   */
  @Throws(SQLException::class) // TODO: wrap exception
  override fun close() {
    if (!isClosed) {
      commit()
    }
  }

  /**
   * Aborts this action.
   */
  @Throws(SQLException::class) // TODO: wrap exception
  fun abort() {
    connection.rollback()
    connection.close()
  }

  /**
   * Commits this action.
   */
  @Throws(SQLException::class) // TODO: wrap exception
  fun commit() {
    connection.commit()
    connection.close()
  }

  /**
   * Sends a message.
   */
  @Throws(SQLException::class) // TODO: wrap exception
  fun sendMessage(messageText: String): Message {
    connection.prepareCall("{?= call conreality.message_send(?)}").use { statement ->
      statement.registerOutParameter(1, Types.BIGINT)
      statement.setString(2, messageText)
      statement.execute()
      return Message(session, statement.getLong(1))
    }
  }
}
