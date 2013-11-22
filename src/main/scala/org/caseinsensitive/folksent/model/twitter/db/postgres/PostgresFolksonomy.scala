package org.caseinsensitive.folksent.model.db.postgres

import org.caseinsensitive.folksent.model._
import org.caseinsensitive.folksent.model.twitter.{TwitterEntities, TwitterReference, FullTweet}
import org.caseinsensitive.folksent.model.twitter.db.postgres.{PGTweet, TweetReferences, Tweets, Users}
import scala.slick.driver.PostgresDriver.simple._
import org.caseinsensitive.folksent.model.twitter.db.postgres.PGTweet

class PostgresFolksonomy(private implicit val session: Session) extends FolksonomyReadable[PGTweet]
    with FolksonomyWritable[FullTweet] {

  def authors(): Seq[Author] = {
    (for { user <- Users } yield user).list
  }

  def documents(): Seq[PGTweet] = {
    (for { tweet <- Tweets } yield tweet).list
  }

  def topics(): Seq[TwitterReference] = {
    (for { ref <- TweetReferences } yield ref.ref_name).list.map(TwitterEntities.createReference)
  }

  def author(tweet: PGTweet): Author = tweet.author

  def topics(tweet: PGTweet): Seq[TwitterReference] = {
    (for { ref <- TweetReferences if ref.tweet_id === tweet.id } yield ref.ref_name).list.map(TwitterEntities.createReference)
  }

  def documents(author: Author): Seq[PGTweet] = {
    (for { tweet <- Tweets if tweet.user_id === author.name } yield tweet).list
  }

  def documents(topic: Topic): Seq[PGTweet] = {
    (for {
      ref <- TweetReferences if ref.ref_name === topic.name
      tweet <- ref.tweet
    } yield tweet).list
  }

  def add(document: FullTweet): Unit = {
    Tweets.insert(new PGTweet(document))
  }

  override def addAll(documents: FullTweet*): Unit = {
    Tweets.insertAll(documents.map(new PGTweet(_)) : _*)
  }
}
