package folksent.storage;

import folksent.model.Folksonomy;
import folksent.model.FolksonomyException;
import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

public interface StorageAdapter<TDocument extends Document, TAuthor extends Author, TTopic extends Topic, TQuery> {

	public Folksonomy<TDocument, TAuthor, TTopic> loadFolksonomy(TQuery query) throws FolksonomyException;
	public void storeFolksonomy(Folksonomy<TDocument, TAuthor, TTopic> folksonomy) throws FolksonomyException;

}
