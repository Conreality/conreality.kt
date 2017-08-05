/* This is free and unencumbered software released into the public domain. */

package org.conreality.sdk

import java.io.IOException
import java.io.OutputStream
import java.sql.SQLException

import org.postgresql.largeobject.LargeObject

/**
 * @constructor
 * @property lo
 * @property callback
 *
 * @see http://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html
 * @eee https://jdbc.postgresql.org/documentation/publicapi/org/postgresql/largeobject/LargeObject.html
 */
class LargeObjectOutputStream(val lo: LargeObject, val callback: (LargeObject) -> Unit) : OutputStream() {

  @Throws(IOException::class)
  override fun close() {
    try {
      lo.close()
      callback(lo)
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
