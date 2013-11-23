package org.caseinsensitive.folksent.model.mongo

import org.caseinsensitive.folksent.model.twitter._
import com.mongodb.DBObject
import org.caseinsensitive.folksent.model.twitter.Reference
import org.caseinsensitive.folksent.model.twitter.Hashtag

case class MongoTweet(underlying: DBObject) extends FullTweet(

  underlying.get("tweet_id") match {
    case n: java.lang.Long => n
    case _ => throw new IllegalArgumentException("tweet missing id")
  },

  (underlying.get("user_id"), underlying.get("username")) match {
    case (user_id: java.lang.Long, username: String) => TwitterAuthor(user_id, username)
    case _ => throw new IllegalArgumentException("tweet missing user info")
  },

  underlying.get("text") match {
    case s: String => s
    case _ => throw new IllegalArgumentException("tweet missing text")
  },

  (underlying.get("hashtags") match {
    case tags: Seq[String] => tags.map(Hashtag).toSet
    case _ => Set[TwitterReference]()
  }) ++
  (underlying.get("references") match {
    case refs: Seq[String] => refs.map(Reference).toSet
    case _ => Set[TwitterReference]()
  })

)

