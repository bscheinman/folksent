package org.caseinsensitive.folksent.model

import org.caseinsensitive.folksent.model.entity.{Entity, Document}
import org.jgrapht.{WeightedGraph, Graph}

object FolksonomyGraph {

  implicit class FolksonomyGraphs[T <: Document](folksonomy: Folksonomy[T]) {

    def toGraph(): Graph[Entity, String] = ???

  }


  implicit class AugmentedFolksonomyGraphs[T <: Document](folksonomy: AugmentedFolksonomy[T]) {

    def toGraph(): WeightedGraph[Entity, String] = ???

  }

}
