@file:JvmName("SDK")
@file:JvmMultifileClass

package org.conreality

class Client() {
  init {
    Class.forName("org.postgresql.Driver").newInstance()
  }
}
