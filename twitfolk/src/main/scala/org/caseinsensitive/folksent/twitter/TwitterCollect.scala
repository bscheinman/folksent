package org.caseinsensitive.folksent.twitter

import org.caseinsensitive.folksent.twitter.TwitterUtils._
import org.caseinsensitive.folksent.model.twitter.TwitterImplicits._
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import org.caseinsensitive.folksent.model.db.postgres.PostgresFolksonomy
import scala.slick.driver.PostgresDriver.simple._

object TwitterCollect {

  def main(args: Array[String]){

    val postgresProperties: Map[String, String] = Map(
      "user" -> "scala_user",
      "password" -> "scala"
    )
    implicit val session = Database.forURL("jdbc:postgresql://localhost:5432/twitter", postgresProperties).createSession

    val twitter = TwitterFactory.getSingleton
    twitter setOAuthConsumer("oauth_consumer", "info")
    twitter setOAuthAccessToken new AccessToken("oauth_access", "info")

    val folksonomy = new PostgresFolksonomy()

    val query = new twitter4j.Query().topics(List("#obama", "#romney", "@BarackObama", "@MittRomney"))
    twitter.processTweets(query, tweet => folksonomy.add(tweet), 0, 404059368703803392L)

  }

}
