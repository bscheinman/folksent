package folksent.twitfolk;

import com.mongodb.*;
import folksent.model.Folksonomy;
import folksent.model.FolksonomyException;
import folksent.model.entity.BaseAuthor;
import folksent.model.entity.BaseTopic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class MongoFolksonomy implements Folksonomy<MongoDocument, BaseAuthor, BaseTopic> {

	@Override
	public Collection<BaseAuthor> getAuthors() {
		MapReduceOutput output = tweetCollection_.mapReduce(getAuthorsMapFn_, getAuthorsReduceFn_, null,
		                                                    MapReduceCommand.OutputType.INLINE, new BasicDBObject());
		Collection<BaseAuthor> authors = new HashSet<>();
		for (DBObject result : output.results()) {
			authors.add(new BaseAuthor(result.get("_id").toString()));
		}
		return authors;
	}

	private final static String getAuthorsMapFn_ = "function() { emit(this.user_id, null); }";
	private final static String getAuthorsReduceFn_ = "function(key, values) { return null; }";


	@Override
	public Collection<MongoDocument> getDocuments() {
		return getDocumentsImpl_(null);
	}


	@Override
	public Collection<BaseTopic> getTopics() {
		MapReduceOutput output = tweetCollection_.mapReduce(getTopicsMapFn_, getTopicsReduceFn_, null,
				MapReduceCommand.OutputType.INLINE, new BasicDBObject());
		Collection<BaseTopic> topics = new HashSet<>();
		for (DBObject result : output.results()) {
			topics.add(new BaseTopic(result.get("_id").toString()));
		}
		return topics;
	}

	private final static String getTopicsMapFn_ = "function() { for (i in this.hashtags) { emit(this.hashtags[i], null); } }";
	private final static String getTopicsReduceFn_ = "function(key, values) { return null; }";


	@Override
	public BaseAuthor getAuthor(MongoDocument document) {
		return new BaseAuthor(document.getMongoObject().get("user_id").toString());
	}


	@Override
	public Collection<MongoDocument> getDocuments(BaseAuthor author) {
		return getDocumentsImpl_(author.getEntityName());
	}


	@Override
	public Collection<BaseTopic> getTopics(MongoDocument document) {
		Collection<BaseTopic> topics = new ArrayList<>();

		BasicDBList hashtagList = (BasicDBList) document.getMongoObject().get("hashtags");
		for (Object obj : hashtagList) {
			topics.add(new BaseTopic(obj.toString()));
		}

		return topics;
	}


	@Override
	public void addDocument(MongoDocument document) throws FolksonomyException {
		// TODO
	}


	@Override
	public void removeDocument(MongoDocument document) {
		// TODO
	}


	public MongoFolksonomy(DBCollection tweetCollection, DBCollection authorCollection) {
		tweetCollection_ = tweetCollection;
		authorCollection_ = authorCollection;
	}


	private DBCollection tweetCollection_;
	private DBCollection authorCollection_;


	private Collection<MongoDocument> getDocumentsImpl_(String user) {
		Collection<MongoDocument> documents = new ArrayList<>();

		BasicDBObject query = new BasicDBObject();
		if (user != null) {
			query.append("user_id", user);
		}

		try (DBCursor cursor = tweetCollection_.find(query)) {
			while (cursor.hasNext()) {
				documents.add(new MongoDocument(cursor.next()));
			}
		} catch (MongoException e) {
			// TODO: Do we want to add a dependency on log4j here?
		}

		return documents;
	}
}
