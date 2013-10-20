package folksent.model;

import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

public interface SentimentExtractor<TDocument extends Document, TAuthor extends Author, TTopic extends Topic> {
    public Double getDocumentSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TDocument document, TTopic topic);
    public Double getAuthorSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TAuthor author, TTopic topic);
}
