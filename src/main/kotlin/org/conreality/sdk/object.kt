/* This is free and unencumbered software released into the public domain. */

package org.conreality.sdk

import java.util.UUID

data class Object(val session: Session, val uuid: UUID) {
  val location: Location?
    get() {
      return null // TODO
    }
}
