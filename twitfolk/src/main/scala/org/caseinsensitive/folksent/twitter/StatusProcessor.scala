package org.caseinsensitive.folksent.twitter

import twitter4j.Status

trait StatusProcessor {
  val processor: Status => Unit
  val filter: Status => Boolean

  def onStatus(status: Status) {
    if (filter(status)) {
      processor(status)
    }
  }
}
