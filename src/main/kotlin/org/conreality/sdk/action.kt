package org.conreality.sdk

import java.sql.SQLException

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
}
