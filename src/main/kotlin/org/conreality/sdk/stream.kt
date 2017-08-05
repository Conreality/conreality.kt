/* This is free and unencumbered software released into the public domain. */

package org.conreality.sdk

import java.io.IOException
import java.io.OutputStream
import java.sql.SQLException
import java.sql.Types

import org.postgresql.PGConnection
import org.postgresql.largeobject.LargeObject
import org.postgresql.largeobject.LargeObjectManager

/**
 * @constructor
 * @property lo
 * @property callback
 *
 * @see http://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html
 * @eee https://jdbc.postgresql.org/documentation/publicapi/org/postgresql/largeobject/LargeObject.html
 */
open class LargeObjectOutputStream(val action: Action) : OutputStream() {
  //assert(connection.isWrapperFor(PGConnection::class.java))
  val connection = action.connection
  val manager = connection.unwrap(PGConnection::class.java).getLargeObjectAPI()
  val oid = manager.createLO(LargeObjectManager.WRITE)
  val lo = manager.open(oid, LargeObjectManager.WRITE)

  @Throws(IOException::class)
  override fun close() {
    try {
      lo.close()
    }
    catch (error: SQLException) {
      throw IOException(error)
    }
  }

  @Throws(IOException::class)
  override fun flush() {
    /* nothing to do */
  }

  @Throws(IOException::class)
  override fun write(bytes: ByteArray) {
    try {
      lo.write(bytes)
    }
    catch (error: SQLException) {
      throw IOException(error)
    }
  }

  @Throws(IOException::class)
  override fun write(bytes: ByteArray, off: Int, len: Int) {
    try {
      lo.write(bytes, off, len)
    }
    catch (error: SQLException) {
      throw IOException(error)
    }
  }

  @Throws(IOException::class)
  override fun write(byte: Int) {
    try {
      lo.write(byteArrayOf(byte.toByte()))
    }
    catch (error: SQLException) {
      throw IOException(error)
    }
  }
}

class BinaryOutputStream<T>(action: Action, val callback: (Binary) -> T) : LargeObjectOutputStream(action) {

  @Throws(IOException::class)
  fun finish(): T {
    try {
      var binaryID = 0L
      connection.prepareCall("{?= call conreality.binary_import(?::oid)}").use { statement ->
        statement.registerOutParameter(1, Types.BIGINT)
        statement.setLong(2, oid)
        statement.execute()
        binaryID = statement.getLong(1)
      }
      assert(binaryID != 0L)
      return callback(Binary(action.session, binaryID))
    }
    finally {
      manager.delete(oid)
    }
  }
}
