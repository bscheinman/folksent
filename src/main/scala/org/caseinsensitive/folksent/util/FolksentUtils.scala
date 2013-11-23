package org.caseinsensitive.folksent.util

import scala.util.control.Exception.catching

object FolksentUtils {

  implicit class ParseableString(s: String) {

    def toIntOpt(): Option[Int] = {
      catching(classOf[NumberFormatException]) opt s.toInt
    }

    def toLongOpt(): Option[Long] = {
      catching(classOf[NumberFormatException]) opt s.toLong
    }

    def toDoubleOpt(): Option[Double] = {
      catching(classOf[NumberFormatException]) opt s.toDouble
    }

  }

}
