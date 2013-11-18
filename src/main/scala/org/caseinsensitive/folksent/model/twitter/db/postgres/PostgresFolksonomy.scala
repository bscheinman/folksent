package org.caseinsensitive.folksent.model.db.postgres

import org.caseinsensitive.folksent.model.{Topic, Author, Folksonomy}
import org.caseinsensitive.folksent.model.twitter.{TwitterReference, FullTweet}

class PostgresFolksonomy extends Folksonomy[FullTweet] {

  def authors(): Seq[Author] = ???

  def documents(): Seq[FullTweet] = ???

  def topics(): Seq[TwitterReference] = ???

  def author(tweet: FullTweet): Author = tweet.author

  def topics(tweet: FullTweet): Seq[TwitterReference] = tweet.refs.toSeq

  def documents(author: Author): Seq[FullTweet] = ???

  def documents(topic: Topic): Seq[FullTweet] = ???

  def add(document: FullTweet): Unit = ???
}
