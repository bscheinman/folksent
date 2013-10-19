package folksent.storage;


import folksent.model.BaseAugmentedFolksonomy;
import folksent.model.FolksonomyException;
import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

public interface AugmentedStorageAdapter<TDocument extends Document, TAuthor extends Author, TTopic extends Topic, TQuery>
		extends StorageAdapter<TDocument, TAuthor, TTopic, TQuery> {

	public void storeFolksonomy(BaseAugmentedFolksonomy<TDocument, TAuthor, TTopic> folksonomy) throws FolksonomyException;

	@Override
	public BaseAugmentedFolksonomy<TDocument, TAuthor, TTopic> loadFolksonomy(TQuery query) throws FolksonomyException;

}
