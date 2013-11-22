package org.caseinsensitive.folksent.model

trait FolksonomyReadable[+T <: Document] {
  def authors(): Seq[Author]
  def documents(): Seq[T]
  def topics(): Seq[Topic] // immutable.Set is annoyingly not covariant

  def author(document: T): Author
  def documents(author: Author): Seq[T]
  def documents(topic: Topic): Seq[T]
  def topics(document: T): Seq[Topic]
}

trait FolksonomyWritable[-T <: Document] {
  def add(document: T): Unit
  def addAll(documents: T*) = documents.foreach(add)

  def +=(document: T) = {
    add(document)
    this
  }
  def +=(documents: Seq[T]) = {
    addAll(documents : _*)
    this
  }
}

trait Folksonomy[T <: Document] extends FolksonomyReadable[T] with FolksonomyWritable[T]


trait AugmentedFolksonomyReadable[+T <: Document] extends FolksonomyReadable[T] {
  def sentiment(document: T, topic: Topic): Double
  def sentiment(author: Author, topic: Topic): Double
}

trait AugmentedFolksonomy[T <: Document] extends Folksonomy[T] with AugmentedFolksonomyReadable[T]
