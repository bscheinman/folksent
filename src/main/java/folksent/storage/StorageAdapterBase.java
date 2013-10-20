package folksent.storage;


import folksent.model.DocumentInformationExtractor;
import folksent.model.Folksonomy;
import folksent.model.FolksonomyException;
import folksent.model.InMemoryFolksonomy;
import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

public abstract class StorageAdapterBase<TDocument extends Document, TAuthor extends Author, TTopic extends Topic, TQuery>
		implements StorageAdapter<TDocument, TAuthor, TTopic, TQuery> {

	@Override
	public Folksonomy<TDocument, TAuthor, TTopic> loadFolksonomy(TQuery query) throws FolksonomyException {
		Folksonomy<TDocument, TAuthor, TTopic> folksonomy = new InMemoryFolksonomy<TDocument, TAuthor, TTopic>(getDocumentExtractor_());
		for (TDocument document : getDocuments_(query)) {
			folksonomy.addDocument(document);
		}

		return folksonomy;
	}


	@Override
	public abstract void storeFolksonomy(Folksonomy<TDocument, TAuthor, TTopic> folksonomy) throws FolksonomyException;

	protected abstract DocumentInformationExtractor<TDocument, TAuthor, TTopic> getDocumentExtractor_();
	protected abstract Iterable<TDocument> getDocuments_(TQuery query);
}
