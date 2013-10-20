package folksent.twitfolk;

import folksent.model.entity.Document;

import java.util.List;

public interface DocumentWordExtractor<TDocument extends Document> {
	public List<String> getWords(TDocument document);
}
