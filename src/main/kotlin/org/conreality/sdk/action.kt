/* This is free and unencumbered software released into the public domain. */

package org.conreality.sdk

import java.io.InputStream
import java.sql.SQLException
import java.sql.Types

/**
 * A transactional action to mutate Conreality master state.
 *
 * @constructor
 * @property session the current session
 */
class Action(val session: Session) : AutoCloseable {
  val connection = session.getConnection()

  val isPending get() = !isClosed

  val isClosed get() = connection.isClosed

  /**
   * This method is idempotent.
   */
  @Throws(TransactionException::class)
  override fun close() {
    if (!isClosed) {
      commit()
    }
  }

  /**
   * Aborts this action.
   */
  @Throws(TransactionException::class)
  fun abort() {
    try {
      connection.rollback()
      connection.close()
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }

  /**
   * Commits this action.
   */
  @Throws(TransactionException::class)
  fun commit() {
    try {
      connection.commit()
      connection.close()
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }

  /**
   * Sends an event.
   */
  @Throws(TransactionException::class)
  fun sendEvent(predicate: String, subject: Object, `object`: Object): Event {
    try {
      connection.prepareCall("{?= call conreality.event_send(?, ?, ?)}").use { statement ->
        statement.registerOutParameter(1, Types.BIGINT)
        statement.setString(2, predicate)
        statement.setString(3, subject.uuid.toString())
        statement.setString(4, `object`.uuid.toString())
        statement.execute()
        return Event(session, statement.getLong(1))
      }
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }

  /**
   * Sends a text message.
   */
  @Throws(TransactionException::class)
  fun sendMessage(messageText: String): Message {
    try {
      connection.prepareCall("{?= call conreality.message_send(?)}").use { statement ->
        statement.registerOutParameter(1, Types.BIGINT)
        statement.setString(2, messageText)
        statement.execute()
        return Message(session, statement.getLong(1))
      }
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }

  /**
   * Sends an audio message.
   */
  @Throws(TransactionException::class)
  fun sendAudioMessage(messageData: ByteArray): Message {
    return sendAudioMessage(messageData.inputStream())
  }

  /**
   * Sends an audio message.
   */
  @Throws(TransactionException::class)
  fun sendAudioMessage(messageStream: InputStream): Message {
    try {
      connection.prepareCall("{?= call conreality.message_send(?::bytea)}").use { statement ->
        statement.registerOutParameter(1, Types.BIGINT)
        statement.setBinaryStream(2, messageStream)
        statement.execute()
        return Message(session, statement.getLong(1))
      }
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }

  /**
   * Begins streaming an audio message.
   */
  @Throws(TransactionException::class)
  fun beginAudioMessage(): BinaryOutputStream<Message> {
    try {
      return BinaryOutputStream(this) { binary: Binary ->
        Message(session, binary.id) // FIXME: message ID instead of binary ID
      }
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }

  /**
   * Reports the agent's current location.
   */
  @Throws(TransactionException::class)
  fun reportLocation(location: Location) {
    try {
      // TODO
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }

  /**
   * Reports the agent's current motion.
   */
  @Throws(TransactionException::class)
  fun reportMotion() {
    try {
      // TODO
    }
    catch (error: SQLException) {
      throw TransactionException(error)
    }
  }
}
