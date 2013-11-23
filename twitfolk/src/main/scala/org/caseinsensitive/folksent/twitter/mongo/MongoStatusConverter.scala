package org.caseinsensitive.folksent.twitter.mongo

import com.mongodb.DBObject
import twitter4j.Status
import com.mongodb.casbah.commons.MongoDBObject

object MongoStatusConverter {

  val userRegex = "@[A-Za-z0-9]+".r

  def toObject(status: Status): DBObject = {
    val builder = MongoDBObject.newBuilder

    builder += "tweet_id" -> status.getId
    builder += "text" -> status.getText
    builder += "timestamp" -> status.getCreatedAt
    builder += "user_id" -> status.getUser.getId
    builder += "username" -> status.getUser.getScreenName
    builder += "hashtags" -> status.getHashtagEntities.map(_.getText)
    builder += "references" -> userRegex.findAllIn(status.getText).map(_.substring(1)).toList
    builder += "urls" -> status.getURLEntities.map(_.getExpandedURL)
    // ignore locations for now

    builder.result
  }

}
