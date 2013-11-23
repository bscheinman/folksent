package org.caseinsensitive.folksent.model.twitter

import org.caseinsensitive.folksent.model.{Topic, Document, Author}
import org.joda.time.DateTime

trait Tweet extends Document {
  val id: Long
  val text: String
  val name = id.toString
}

case class FullTweet(id: Long, author: TwitterAuthor, timestamp: DateTime, text: String, refs: Set[TwitterReference]) extends Tweet


abstract class TwitterReference extends Topic

case class Hashtag(content: String) extends TwitterReference {
  val name = s"#${content}"
}

case class Reference(content: String) extends TwitterReference {
  val name = s"@${content}"
}


case class TwitterAuthor(id: Long, username: String) extends Author {
  val name = id.toString
}

object TwitterEntities {
  def createReference(ref_name: String): TwitterReference = {
    ref_name match {
      case s: String if s.startsWith("#") => Hashtag(s.substring(1))
      case s: String if s.startsWith("@") => Reference(s.substring(1))
      case _ => throw new IllegalArgumentException("Unrecognized Twitter reference type")
    }
  }
}
