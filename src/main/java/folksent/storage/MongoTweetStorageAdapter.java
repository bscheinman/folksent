package folksent.storage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import folksent.model.BaseAugmentedFolksonomy;
import folksent.model.BaseFolksonomy;
import folksent.model.DocumentInformationExtractor;
import folksent.model.SentimentExtractor;
import folksent.model.entity.Author;
import folksent.model.entity.BaseAuthor;
import folksent.model.entity.BaseTopic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MongoTweetStorageAdapter extends AugmentedStorageAdapterBase<MongoDocument, BaseAuthor, BaseTopic, Object> {

	@Override
	public void storeFolksonomy(BaseFolksonomy<MongoDocument, BaseAuthor, BaseTopic> folksonomy) {
		// nothing to do here because we just copied the relations from the existing information in the database
	}


	@Override
	public void storeFolksonomy(BaseAugmentedFolksonomy<MongoDocument, BaseAuthor, BaseTopic> folksonomy) {
		for (MongoDocument document : folksonomy.getDocuments()) {
			BasicDBObject documentSentiments = new BasicDBObject();
			for (BaseTopic topic : folksonomy.getTopics(document)) {
				documentSentiments.append(topic.getEntityName(), folksonomy.getDocumentSentiment(document, topic));
			}

			BasicDBObject documentUpdate = new BasicDBObject("$set", new BasicDBObject("sentiments", documentSentiments));

			tweetCollection_.update(new BasicDBObject("_id", document.getEntityName()), documentUpdate);
		}

		for (Author author : folksonomy.getAuthors()) {
			// TODO
		}
	}


	public MongoTweetStorageAdapter(DBCollection tweetCollection, DBCollection authorCollection) {
		tweetCollection_ = tweetCollection;
		authorCollection_ = authorCollection;
	}


	@Override
	protected DocumentInformationExtractor<MongoDocument, BaseAuthor, BaseTopic> getDocumentExtractor_() {
		return new DocumentInformationExtractor<MongoDocument, BaseAuthor, BaseTopic>() {

			@Override
			public BaseAuthor extractAuthor(MongoDocument document) {
				return new BaseAuthor((String) document.getMongoObject().get("author"));
			}

			@Override
			public Set<BaseTopic> extractTopics(MongoDocument document) {
				Set<BaseTopic> topics = new HashSet<BaseTopic>();
				for (Object topicObj : (ArrayList<Object>) document.getMongoObject().get("hashtags")) {
					topics.add(new BaseTopic((String) topicObj));
				}

				return topics;
			}
		};
	}

	@Override
	protected SentimentExtractor<MongoDocument, BaseAuthor, BaseTopic> getSentimentExtractor_() {
		return new SentimentExtractor<MongoDocument, BaseAuthor, BaseTopic>() {
			@Override
			public Double getDocumentSentiment(BaseFolksonomy<MongoDocument, BaseAuthor, BaseTopic> folksonomy, MongoDocument mongoDocument, BaseTopic topic) {
				// TODO
				return null;
			}

			@Override
			public Double getAuthorSentiment(BaseFolksonomy<MongoDocument, BaseAuthor, BaseTopic> folksonomy, BaseAuthor author, BaseTopic topic) {
				// TODO
				return null;
			}
		};
	}


	@Override
	protected Collection<MongoDocument> getDocuments_(Object query) {
		Collection<MongoDocument> documents = new ArrayList<MongoDocument>();
		DBCursor cursor = tweetCollection_.find();
		try {
			while (cursor.hasNext()) {
				documents.add(new MongoDocument(cursor.next()));
			}
		} catch (MongoException e) {
			// TODO: Do we want to add a dependency on log4j here?
		} finally {
			cursor.close();
		}

		return documents;
	}


	private DBCollection tweetCollection_;
	private DBCollection authorCollection_;
}
