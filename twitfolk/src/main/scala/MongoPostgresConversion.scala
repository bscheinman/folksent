import org.caseinsensitive.folksent.model.twitter.db.postgres.{UserSentiments, TweetReferences, Users, Tweets}
import scala.slick.driver.PostgresDriver.simple._

import Database.threadLocalSession


object MongoPostgresConversion {

  def main(args: String*): Unit = {
    (Tweets.ddl ++ Users.ddl ++ TweetReferences.ddl ++ UserSentiments.ddl).create


  }

}
