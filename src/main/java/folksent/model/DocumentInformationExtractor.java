package folksent.model;

import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

import java.util.Collection;

public interface DocumentInformationExtractor<TDocument extends Document, TAuthor extends Author, TTopic extends Topic> {
	public TAuthor extractAuthor(TDocument document);
	public Collection<TTopic> extractTopics(TDocument document);
}
