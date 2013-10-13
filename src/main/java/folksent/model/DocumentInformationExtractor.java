package folksent.model;

import java.util.Set;

public interface DocumentInformationExtractor
	<TDocument extends Document, TAuthor extends Author, TTopic extends Topic> {
	public TAuthor extractAuthor(TDocument document);
	public Set<TTopic> extractTopics(TDocument document);
}
