package org.caseinsensitive.folksent.model

import org.caseinsensitive.folksent.model.entity.{Topic, Author, Document}

trait Folksonomy[T <: Document] {

  def authors(): Seq[Author]
  def documents(): Seq[T]
  def topics[TTopic <: Topic](): Set[TTopic]

  def author(document: T): Author
  def documents(author: Author): Seq[T]
  def documents(topic: Topic): Seq[T]
  def topics[TTopic <: Topic](document: T): Set[TTopic]

  def add(document: T): Unit
  def +=(document: T) = {
    this add document
    this
  }

}


trait AugmentedFolksonomy[T <: Document] extends Folksonomy[T] {
  def sentiment(document: T, topic: Topic): Double
  def sentiment(author: Author, topic: Topic): Double
}
