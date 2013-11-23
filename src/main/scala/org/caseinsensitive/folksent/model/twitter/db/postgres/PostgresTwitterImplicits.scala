package org.caseinsensitive.folksent.model.twitter.db.postgres

import org.caseinsensitive.folksent.model.twitter.FullTweet
import java.sql.Timestamp

object PostgresTwitterImplicits {

  implicit def asPostgresTweet(tweet: FullTweet): PGTweet = {
    PGTweet(tweet.id, tweet.author.id, Some(new Timestamp(tweet.timestamp.getMillis)), tweet.text)
  }

}
