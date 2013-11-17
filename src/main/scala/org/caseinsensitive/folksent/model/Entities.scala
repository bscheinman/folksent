package org.caseinsensitive.folksent.model

trait Entity {
  val name: String
  override def hashCode(): Int = {
    name.hashCode
  }
}

trait Author extends Entity
case class BaseAuthor(name: String) extends Author

trait Document extends Entity

trait Topic extends Entity
case class BaseTopic(name: String) extends Topic
