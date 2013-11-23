package org.caseinsensitive.folksent.model.twitter

import twitter4j.{User, Status}
import org.joda.time.DateTime

object TwitterImplicits {

  implicit def asFolksonomyUser(user: User): TwitterAuthor = {
    TwitterAuthor(user.getId, user.getScreenName)
  }

  val userRegex = "@[a-zA-Z0-9_]{1,15}".r
  implicit def asFolksonomyTweet(tweet: Status): FullTweet = {
    FullTweet(tweet.getId, tweet.getUser, new DateTime(tweet.getCreatedAt), tweet.getText,
      (tweet.getHashtagEntities.map(tag => Hashtag(tag.getText)) ++
        userRegex.findAllIn(tweet.getText).map(ref => Reference(ref.substring(1)))).toSet)
  }

}
