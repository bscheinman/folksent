package folksent.model;

import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

import java.util.Collection;

public interface Folksonomy<TDocument extends Document, TAuthor extends Author, TTopic extends Topic> {

	public Collection<TAuthor> getAuthors();
	public Collection<TDocument> getDocuments();
	public Collection<TTopic> getTopics();

	public Collection<TDocument> getDocuments(TAuthor author);
	public Collection<TTopic> getTopics(TDocument document);

	public void addDocument(TDocument document) throws FolksonomyException;
	public void removeDocument(TDocument document);

}
