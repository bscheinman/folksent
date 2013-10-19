package folksent.storage;

import com.mongodb.DBObject;
import folksent.model.entity.Document;

public class MongoDocument implements Document {

	public String getEntityName() {
		return ((String) document_.get(NAME_KEY_));
	}

	public DBObject getMongoObject() {
		return document_;
	}

	public MongoDocument(DBObject document) {
		document_ = document;
	}

	private DBObject document_;

	private static final String NAME_KEY_ = "_id";
}
