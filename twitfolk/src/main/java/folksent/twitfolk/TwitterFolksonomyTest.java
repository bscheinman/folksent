package folksent.twitfolk;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import folksent.model.Folksonomy;
import folksent.model.InMemoryFolksonomy;
import folksent.model.SentimentExtractor;
import folksent.model.entity.BaseAuthor;
import folksent.model.entity.BaseTopic;

import java.io.File;
import java.util.Collection;

public class TwitterFolksonomyTest {

	public static void main(String[] args) throws Exception {
		MongoClient client = new MongoClient("localhost", 27017);
		DB db = client.getDB("twitter_stream");
		DBCollection tweetCollection = db.getCollection("tweets");
		DBCollection authorCollection = db.getCollection("authors");

		WordSentimentMap sentimentMap = new WordSentimentMap();
		sentimentMap.loadFromFile(new File("/home/brendon/data/twitter/sentiments.txt"));

		SentimentExtractor<MongoDocument, BaseAuthor, BaseTopic> extractor =
				new BasicSentimentExtractor<>(sentimentMap, new MongoTweetWordExtractor());

		Folksonomy<MongoDocument, BaseAuthor, BaseTopic> folksonomy =
				new MongoFolksonomy(tweetCollection, authorCollection);

		// This loads the database information into memory
		folksonomy = new InMemoryFolksonomy<>(folksonomy);

		for (MongoDocument document : folksonomy.getDocuments()) {
			Collection<BaseTopic> topics = folksonomy.getTopics(document);
			if (topics.isEmpty()) {
				continue;
			}

			System.out.println(String.format("%s: %f", document.getEntityName(), extractor.getDocumentSentiment(
					folksonomy, document, topics.iterator().next())));

			for (BaseTopic topic : topics) {
				System.out.println(String.format("\t%s", topic.getEntityName()));
			}
		}
	}

}
