package folksent.model;

public interface DocumentTopicSentimentExtractor<TDocument extends Document, TTopic extends Topic> {
	public double calculateSentiment(TDocument document, TTopic topic);
}
