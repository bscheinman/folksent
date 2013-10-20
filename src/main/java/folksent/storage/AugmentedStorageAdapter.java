package folksent.storage;


import folksent.model.AugmentedFolksonomy;
import folksent.model.FolksonomyException;
import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

public interface AugmentedStorageAdapter<TDocument extends Document, TAuthor extends Author, TTopic extends Topic, TQuery>
		extends StorageAdapter<TDocument, TAuthor, TTopic, TQuery> {

	public void storeFolksonomy(AugmentedFolksonomy<TDocument, TAuthor, TTopic> folksonomy) throws FolksonomyException;

	@Override
	public AugmentedFolksonomy<TDocument, TAuthor, TTopic> loadFolksonomy(TQuery query) throws FolksonomyException;

}
