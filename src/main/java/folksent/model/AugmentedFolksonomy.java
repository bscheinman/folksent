package folksent.model;

import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

public interface AugmentedFolksonomy<TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
		extends Folksonomy<TDocument, TAuthor, TTopic> {

	public Double getDocumentSentiment(TDocument document, TTopic topic);
	public Double getAuthorSentiment(TAuthor author, TTopic topic);

}
