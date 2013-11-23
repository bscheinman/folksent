import com.mongodb.casbah.MongoConnection
import org.caseinsensitive.folksent.model.db.postgres.PostgresFolksonomy
import org.caseinsensitive.folksent.model.mongo.MongoFolksonomy
import org.caseinsensitive.folksent.model.twitter.db.postgres._
import scala.slick.driver.PostgresDriver.simple._


object MongoPostgresConversion {

  def main(args: Array[String]): Unit = {
    val postgresProperties: Map[String, String] = Map(
      "user" -> "scala_user",
      "password" -> "scala"
    )
    implicit val session = Database.forURL("jdbc:postgresql://localhost:5432/twitter", postgresProperties).createSession
    val mongoConnection = MongoConnection("localhost", 27017)
    implicit val mongoDb = mongoConnection("twitter_scala")

    //(Tweets.ddl ++ Users.ddl ++ TweetReferences.ddl ++ UserSentiments.ddl).create

    val mongoFolksonomy = new MongoFolksonomy
    val postgresFolksonomy = new PostgresFolksonomy

    // TODO: Add pagination
    mongoFolksonomy.documents.foreach(postgresFolksonomy.add)
  }

}
