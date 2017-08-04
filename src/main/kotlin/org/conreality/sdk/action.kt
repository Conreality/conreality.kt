package org.conreality.sdk

/**
 * A transactional action to mutate Conreality master state.
 *
 * @constructor
 * @property session the current session
 */
class Action(val session: Session) {
  private val connection = session.getConnection()

  val isPending get() = !connection.isClosed

  /**
   * Aborts this action.
   */
  fun abort() {
    connection.rollback()
    connection.close()
  }

  /**
   * Commits this action.
   */
  fun commit() {
    connection.commit()
    connection.close()
  }
}
