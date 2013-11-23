package org.caseinsensitive.folksent.twitter.mongo

import com.mongodb.casbah.MongoCollection
import com.mongodb.DBObject
import twitter4j.Status
import org.caseinsensitive.folksent.twitter.StatusProcessor

trait MongoStatusProcessor extends StatusProcessor {
  val collection: MongoCollection
  val converter: Status => DBObject
  val processor: Status => Unit = (s: Status) => collection.insert(s)(converter)
}
