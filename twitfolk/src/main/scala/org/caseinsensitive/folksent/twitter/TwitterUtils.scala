package org.caseinsensitive.folksent.twitter

import _root_.twitter4j._
import scala.collection.JavaConversions._
import scala.annotation.tailrec
import java.text.SimpleDateFormat

object TwitterUtils {

  implicit class TwitterStreamQuery(stream: TwitterStream) {
    def getTopics(topics: String*): Unit = {
      val query = new FilterQuery() track topics.map("#" + _).toArray
      stream filter query
    }
  }

  implicit class QueryBuilder(query: Query) {
    def topics(topics: Seq[String]) = {
      query query topics.mkString(" OR ")
    }
  }

  implicit class TwitterSearchQuery(twitter: Twitter) {

    val dateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm")

    @tailrec final def processFullUserTimeline(
                                                username: String,
                                                processor: Status => Unit,
                                                page: Int = 1): Unit = {

      val tweets = twitter.getUserTimeline(username, new Paging(page))

      tweets.foreach(tweet => {
        if (!tweet.isRetweet) {
          processor(tweet)
          println(s"processed tweet ${tweet.getId} for user ${username} (${dateFormat.format(tweet.getCreatedAt)})")
        }
      })

      if (tweets.size > 0) {
        Thread sleep 5000
        processFullUserTimeline(username, processor, page + 1)
      }
    }


    @tailrec final def processTweets(
                                     baseQuery: Query,
                                     processor: Status => Unit,
                                     sinceId: Long = 0,
                                     untilId: Long = Long.MaxValue): Unit = {

      val query = baseQuery
        .count(100)
        .lang("en")
        .maxId(untilId)

      var result: QueryResult = null
      while (result == null) {
        try {
          result = twitter.search(query)
        } catch {
          case e: Exception => {
            System.err.println("Encountered exception... trying again in 5 seconds")
            Thread sleep 5000
          }
        }
      }

      var minId: Long = Long.MaxValue
      result.getTweets foreach (tweet => {
        if (tweet.getId > sinceId) {
          if (!tweet.isRetweet) {
            processor(tweet)
          }
          println(s"processed tweet ${tweet.getId} (${dateFormat.format(tweet.getCreatedAt)})")
        }
        minId = Math.min(minId, tweet.getId)
      })

      if (result.getCount == 0 || minId <= sinceId) {
        return
      }

      Thread sleep 2000
      processTweets(baseQuery, processor, sinceId, minId - 1)
    }
  }

}
