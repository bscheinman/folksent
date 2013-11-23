package org.caseinsensitive.folksent.twitter.mongo

import com.mongodb.casbah.MongoDB
import com.mongodb.casbah.commons.MongoDBObject

object MongoTwitterQuery {

  val userCountMapper =
    """
      |function() {
      |  emit(this.user_name, 1);
      |}
    """.stripMargin

  val userCountReducer =
    """
      |function(username, counts) {
      |  return Array.sum(counts);
      |}
    """.stripMargin

  implicit class MongoTwitterQueries(db: MongoDB) {

    // Casbah inline M/R doesn't seem to work, so we'll have to run the actual M/R job via the shell
    def getTopUsers(): Seq[String] = {
      val users = db("user_counts").find().sort(MongoDBObject("value" -> -1)).limit(100)
      users.toSeq.map(_.get("_id") match {
        case s: String => s
        case _ => throw new ClassCastException
      })
    }


  }

}

