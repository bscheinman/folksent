package folksent.twitfolk;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
import folksent.model.Folksonomy;
import folksent.model.SentimentExtractor;
import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

import java.util.List;

public class BasicSentimentExtractor<TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
		implements SentimentExtractor<TDocument, TAuthor, TTopic> {

	@Override
	public Double getDocumentSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TDocument document, TTopic topic) {

		// If the document doesn't contain the topic, there is no related sentiment
		if (!folksonomy.getTopics(document).contains(topic)) {
			return null;
		}

		// Otherwise, just average the sentiment of all words in the document (ignoring all context)
		List<String> words = wordExtractor_.getWords(document);
		DoubleArrayList wordSentiments = new DoubleArrayList(words.size());
		for (String word : words) {
			Double wordSentiment = sentimentMap_.getWordSentiment(word);
			if (wordSentiment != null) {
				wordSentiments.add(wordSentiment);
			}
		}

		return wordSentiments.isEmpty() ? null : Descriptive.mean(wordSentiments);
	}

	@Override
	public Double getAuthorSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TAuthor author, TTopic topic) {
		DoubleArrayList documentSentiments = new DoubleArrayList();
		for (TDocument document : folksonomy.getDocuments(author)) {
			Double sentiment = getDocumentSentiment(folksonomy, document, topic);
			if (sentiment != null) {
				documentSentiments.add(sentiment);
			}
		}

		// Likewise, simply average sentiments for all of an author's documents (or return null if the author hasn't written about the topic)
		return documentSentiments.isEmpty() ? null : Descriptive.mean(documentSentiments);
	}


	public BasicSentimentExtractor(WordSentimentMap sentimentMap, DocumentWordExtractor<? super TDocument> wordExtractor) {
		sentimentMap_ = sentimentMap;
		wordExtractor_ = wordExtractor;
	}

	private WordSentimentMap sentimentMap_;
	private DocumentWordExtractor<? super TDocument> wordExtractor_;
}
