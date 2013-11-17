package org.caseinsensitive.folksent.model.entity

trait Entity {
  val name: String
  override def hashCode(): Int = {
    name.hashCode
  }
}

trait Author extends Entity

trait Document extends Entity

trait Topic extends Entity
