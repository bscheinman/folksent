package org.caseinsensitive.folksent.model.twitter.db.postgres

import scala.slick.driver.PostgresDriver.simple._
import org.caseinsensitive.folksent.model.{Document, BaseAuthor}
import org.caseinsensitive.folksent.model.twitter.{TwitterAuthor, FullTweet}


case class PGTweet(id: Long, user_id: Long, text: String) extends Document {
  val name = id.toString
  lazy val author = BaseAuthor(user_id.toString)
  def this(tweet: FullTweet) = {
    this(tweet.id, tweet.author.id, tweet.text)
  }
}
object Tweets extends Table[PGTweet]("tweets") {
  def tweet_id = column[Long]("tweet_id", O.PrimaryKey)
  def user_id = column[Long]("user_id", O.NotNull)
  def text = column[String]("text", O.NotNull)
  def * = tweet_id ~ user_id ~ text <> (PGTweet, PGTweet.unapply _)

  def user = foreignKey("fk_tweet_user", user_id, Users)(_.user_id)
  def user_index = index("ix_tweet_user", (user_id), unique = false)
}


object Users extends Table[TwitterAuthor]("users") {
  def user_id = column[Long]("user_id", O.PrimaryKey)
  def username = column[String]("username", O.NotNull)
  def * = user_id ~ username <> (TwitterAuthor, TwitterAuthor.unapply _)

  def user_index = index("ix_user_name", (username), unique = true)
}


case class TweetReference(tweet_id: Long, ref_name: String, sentiment: Option[Double])
object TweetReferences extends Table[TweetReference]("tweet_references") {
  def tweet_id = column[Long]("tweet_id", O.NotNull)
  def ref_name = column[String]("ref_name", O.NotNull)
  def sentiment = column[Double]("sentiment", O.Nullable)
  def * = tweet_id ~ ref_name ~ sentiment.? <> (TweetReference, TweetReference.unapply _)

  def tweet = foreignKey("fk_tweet_ref_tweet_id", tweet_id, Tweets)(_.tweet_id)
  def tweet_index = index("ix_tweet_ref_tweet_id", (tweet_id), unique = false)
  def ref_index = index("ix_tweet_ref_ref_name", (ref_name), unique = false)
  def unique_constraint = index("uq_tweet_ref", (tweet_id, ref_name), unique = true)
}


case class UserSentiment(user_id: Long, ref_name: String, sentiment: Double)
object UserSentiments extends Table[UserSentiment]("user_sentiments") {
  def user_id = column[Long]("user_id", O.NotNull)
  def ref_name = column[String]("ref_name", O.NotNull)
  def sentiment = column[Double]("sentiment", O.NotNull)
  def * = user_id ~ ref_name ~ sentiment <> (UserSentiment, UserSentiment.unapply _)

  def user = foreignKey("fk_user_sentiment_user_id", user_id, Users)(_.user_id)
  def user_index = index("ix_user_sentiment_user_id", (user_id), unique = false)
  def ref_index = index("ix_user_sentiment_ref_name", (ref_name), unique = false)
  def unique_constraint = index("uq_user_sentiment", (user_id, ref_name), unique = true)
}

