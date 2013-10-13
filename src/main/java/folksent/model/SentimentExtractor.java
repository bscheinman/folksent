package folksent.model;

public interface SentimentExtractor<TDocument extends Document, TAuthor extends Author, TTopic extends Topic> {
    public Double getSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TDocument document, TTopic topic);
    public Double getSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TAuthor author, TTopic topic);
}
