package org.caseinsensitive.folksent.twitter.mongo

import com.mongodb.casbah.MongoCollection
import com.mongodb.DBObject
import twitter4j.{Status, StatusDeletionNotice, StallWarning, StatusListener}

class MongoStatusListener(val collection: MongoCollection,
                                  val filter: Status => Boolean,
                                  val converter: Status => DBObject) extends StatusListener with MongoStatusProcessor {

  override def onException(ex: Exception) {
    printf("twitter error: %s%n", ex.getMessage)
  }

  override def onStallWarning(p1: StallWarning) {

  }

  override def onScrubGeo(p1: Long, p2: Long) {

  }

  override def onTrackLimitationNotice(p1: Int) {

  }

  override def onDeletionNotice(p1: StatusDeletionNotice) {

  }

}
