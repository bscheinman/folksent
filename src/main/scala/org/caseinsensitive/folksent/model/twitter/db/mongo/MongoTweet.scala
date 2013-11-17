package org.caseinsensitive.folksent.model.mongo

import com.mongodb.DBObject
import org.caseinsensitive.folksent.model.twitter.{Hashtag, Reference, TwitterReference, FullTweet}

case class MongoTweet(underlying: DBObject) extends FullTweet(

  underlying.get("tweet_id") match {
    case n: Long => n
    case _ => throw new IllegalArgumentException("tweet missing id")
  },

  underlying.get("user_id") match {
    case n: Long => n
    case _ => throw new IllegalArgumentException("tweet missing user id")
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

