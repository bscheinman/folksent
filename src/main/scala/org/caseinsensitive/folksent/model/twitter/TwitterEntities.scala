package org.caseinsensitive.folksent.model.twitter

import org.caseinsensitive.folksent.model.{Topic, Document, BaseAuthor, Author}

trait Tweet extends Document {
  val id: Long
  val text: String
  val name = id.toString
}

class FullTweet(val id: Long, val user_id: Long, val text: String, val refs: Set[TwitterReference]) extends Tweet {
  val author: Author = BaseAuthor(user_id.toString)
  def unapply(): Option[(Long, Long, String, Set[TwitterReference])] = {
    Some(id, user_id, text, refs)
  }
}


abstract class TwitterReference extends Topic

case class Hashtag(val name: String) extends TwitterReference {
  override def toString() = s"#${name}"
}

case class Reference(val name: String) extends TwitterReference {
  override def toString() = s"@${name}"
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
