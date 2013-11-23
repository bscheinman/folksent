package org.caseinsensitive.folksent.model.mongo

import org.caseinsensitive.folksent.model.twitter._
import com.mongodb.casbah.commons.MongoDBList
import com.mongodb.casbah.commons.Implicits.wrapDBObj
import com.mongodb.DBObject
import java.util.Date
import org.joda.time.DateTime


class MongoTweet(underlying: DBObject) extends FullTweet(

  underlying.getAsOrElse[Long]("tweet_id", throw new IllegalArgumentException("tweet missing id")),

  (underlying.getAs[Long]("user_id"), underlying.getAs[String]("username")) match {
    case (Some(user_id: Long), Some(username: String)) => TwitterAuthor(user_id, username)
    case _ => throw new IllegalArgumentException("tweet missing user info")
  },

  new DateTime(underlying.getAsOrElse[Date]("timestamp", throw new IllegalArgumentException("tweet missing timestamp"))),

  underlying.getAsOrElse[String]("text", throw new IllegalArgumentException("tweet missing text")),

  (underlying.getAsOrElse[MongoDBList]("hashtags", MongoDBList()).map(tag => Hashtag(tag.asInstanceOf[String])) ++
    underlying.getAsOrElse[MongoDBList]("references", MongoDBList()).map(ref => Reference(ref.asInstanceOf[String]))).toSet

)

