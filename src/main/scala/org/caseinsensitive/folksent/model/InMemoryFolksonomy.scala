package org.caseinsensitive.folksent.model

import org.caseinsensitive.folksent.model.entity.{Topic, Entity, Author}
import scala.collection.mutable

class InMemoryFolksonomy[T](val authorExtractor: T => Author, val topicExtractor: T => Seq[Topic]) extends Folksonomy[T] {

  def authors(): Seq[Author] = {
    _authors.keySet.toSeq
  }

  def documents(): Seq[T] = {
    _documents.toSeq
  }

  def topics(): Set[Topic] = {
    _topics.keySet
  }

  def documents(author: Author): Seq[T] = {
    _authors.getOrElse(author, Set.empty[T]).toSeq
  }

  def documents(topic: Topic): Seq[T] = {
    _topics.getOrElse(topic, Set.empty[T]).toSeq
  }

  def add(document: T): Unit = {
    if (!_documents.add(document)) {
      return
    }

    _authors(authorExtractor(document)).+=(document)
  }

  // main document store
  private val _documents = new mutable.HashSet[T]

  // reverse lookups for authors and topics
  private class DocumentReverseLookup[TLookup <: Entity] extends mutable.HashMap[TLookup, mutable.Set[T]] {
    override def default(key: TLookup): mutable.Set[T] = {
      new mutable.HashSet[T]
    }
  }

  private val _authors = new DocumentReverseLookup[Author]
  private val _topics  = new DocumentReverseLookup[Topic]
}
