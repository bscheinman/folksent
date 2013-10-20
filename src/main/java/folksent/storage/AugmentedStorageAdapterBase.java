package folksent.storage;

import folksent.model.*;
import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

public abstract class AugmentedStorageAdapterBase<TDocument extends Document, TAuthor extends Author, TTopic extends Topic, TQuery>
		extends StorageAdapterBase<TDocument, TAuthor, TTopic, TQuery> implements AugmentedStorageAdapter<TDocument, TAuthor, TTopic, TQuery> {

	@Override
	public void storeFolksonomy(AugmentedFolksonomy<TDocument, TAuthor, TTopic> folksonomy) {
		// TODO
	}

	@Override
	public void storeFolksonomy(Folksonomy<TDocument, TAuthor, TTopic> folksonomy) {
		// TODO
	}

	@Override
	public AugmentedFolksonomy<TDocument, TAuthor, TTopic> loadFolksonomy(TQuery query) throws FolksonomyException {
		AugmentedFolksonomy<TDocument, TAuthor, TTopic> folksonomy = new InMemoryAugmentedFolksonomy<>(
				getDocumentExtractor_(),
				getSentimentExtractor_()
		);

		for (TDocument document : getDocuments_(query)) {
			folksonomy.addDocument(document);
		}

		return folksonomy;
	}

	@Override
	protected abstract DocumentInformationExtractor<TDocument, TAuthor, TTopic> getDocumentExtractor_();
	protected abstract SentimentExtractor<TDocument, TAuthor, TTopic> getSentimentExtractor_();

	@Override
	protected abstract Iterable<TDocument> getDocuments_(TQuery tQuery);
}
