package org.caseinsensitive.folksent.model.mongo

import org.caseinsensitive.folksent.model._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoDB
import com.mongodb.casbah.commons.MongoDBObject
import org.caseinsensitive.folksent.model.twitter.TwitterReference
import org.caseinsensitive.folksent.model.twitter.Hashtag
import org.caseinsensitive.folksent.model.BaseAuthor
import org.caseinsensitive.folksent.model.twitter.Reference

class MongoFolksonomy(implicit val db: MongoDB) extends FolksonomyReadable[MongoTweet] {

  def authors(): Seq[Author] = {
    db("user_counts").find(MongoDBObject(), MongoDBObject("_id" -> 1)).map(user => BaseAuthor(user.get("_id").asInstanceOf[String])).toSeq
  }

  // sample < 1 => take all
  def documents() = documents(0)
  def documents(sample: Int): Seq[MongoTweet] = {
    var cursor = db("tweets").find()
    if (sample > 0) {
      cursor = cursor.limit(sample)
    }
    cursor.map(new MongoTweet(_)).toSeq
  }

  def topics(): Seq[Topic] = ???

  def author(document: MongoTweet): Author = {
    document.author
  }

  def topics(document: MongoTweet): Seq[TwitterReference] = {
    document.refs.toSeq
  }

  def _documentSearch(query: DBObject): Seq[MongoTweet] = {
    db("tweets").find(MongoDBObject(), query).map(new MongoTweet(_)).toSeq
  }

  def documents(author: Author): Seq[MongoTweet] = {
    _documentSearch(MongoDBObject("username" -> author.name))
  }

  def documents(topic: Topic): Seq[MongoTweet] = {
    topic match {
      case Hashtag(name) => _documentSearch(MongoDBObject("hashtags" -> name))
      case Reference(name) => _documentSearch(MongoDBObject("references" -> name))
      case _ => Nil
    }
  }

}
