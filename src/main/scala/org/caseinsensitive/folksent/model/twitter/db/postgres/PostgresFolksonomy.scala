package org.caseinsensitive.folksent.model.db.postgres

import org.caseinsensitive.folksent.model.Folksonomy
import org.caseinsensitive.folksent.model.entity.{Topic, Author}
import org.caseinsensitive.folksent.model.twitter.{TwitterReference, FullTweet}

class PostgresFolksonomy extends Folksonomy[FullTweet] {

  def authors(): Seq[Author] = ???

  def documents(): Seq[FullTweet] = ???

  def topics(): Set[TwitterReference] = ???

  def author(tweet: FullTweet): Author = tweet.author

  def topics(tweet: FullTweet): Set[TwitterReference] = tweet.refs

  def documents(author: Author): Seq[FullTweet] = ???

  def documents(topic: Topic): Seq[FullTweet] = ???

  def add(document: FullTweet): Unit = ???
}
