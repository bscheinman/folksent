package folksent.model;

import folksent.model.entity.FolksonomyEntity;

import java.util.Set;

public interface DocumentInformationExtractor<TDocument extends FolksonomyEntity, TAuthor extends FolksonomyEntity, TTopic extends FolksonomyEntity> {
	public TAuthor extractAuthor(TDocument document);
	public Set<TTopic> extractTopics(TDocument document);
}
