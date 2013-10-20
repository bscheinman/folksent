package folksent.model;

import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryAugmentedFolksonomy<TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
        extends InMemoryFolksonomy<TDocument, TAuthor, TTopic>
		implements AugmentedFolksonomy<TDocument, TAuthor, TTopic> {


	public Double getDocumentSentiment(TDocument document, TTopic topic) {
		return documentSentiments_.get(new FolksonomyEdge<>(document, topic));
	}

	public Double getAuthorSentiment(TAuthor author, TTopic topic) {
		ensureAuthorClean_(author);
		return authorSentiments_.get(new FolksonomyEdge<>(author, topic));
	}


	public InMemoryAugmentedFolksonomy(
			DocumentInformationExtractor<TDocument, TAuthor, TTopic> documentExtractor,
			SentimentExtractor<TDocument, TAuthor, TTopic> sentimentExtractor) {
		super(documentExtractor);
		sentimentExtractor_ = sentimentExtractor;
		documentSentiments_ = new HashMap<>();
		authorSentiments_ = new HashMap<>();
		dirtyAuthors_ = new HashSet<>();
	}


	public InMemoryAugmentedFolksonomy(AugmentedFolksonomy<TDocument, TAuthor, TTopic> other) throws FolksonomyException {
		super(new FolksonomyUtils.CopyDocumentInformationExtractor<>(other));
		sentimentExtractor_ = new FolksonomyUtils.CopySentimentExtractor<>(other);
		dirtyAuthors_ = new HashSet<>();
		FolksonomyUtils.copyFolksonomy(other, this);
	}

    private SentimentExtractor<TDocument, TAuthor, TTopic> sentimentExtractor_;

    // TODO: Maybe change these to instead store edge weights as part of the value
    // in the same data structures as the base Folksonomy class?
    private Map<FolksonomyEdge<TDocument, TTopic>, Double> documentSentiments_;
	private Map<FolksonomyEdge<TAuthor, TTopic>, Double> authorSentiments_;

	// TODO: Cache dirty author-topic pairs instead of just authors
	private Set<TAuthor> dirtyAuthors_;


	@Override
	protected void addAuthorDocument_(TAuthor author, TDocument document) {
		super.addAuthorDocument_(author, document);
		dirtyAuthors_.add(author);
	}


	@Override
	protected void addDocumentTopic_(TDocument document, TTopic topic) {
		super.addDocumentTopic_(document, topic);
		documentSentiments_.put(new FolksonomyEdge<>(document, topic),
				sentimentExtractor_.getDocumentSentiment(this, document, topic));
	}


	private void calculateAuthorSentiment_(TAuthor author, TTopic topic) {
		Double sentiment = sentimentExtractor_.getAuthorSentiment(this, author, topic);
		FolksonomyEdge<TAuthor, TTopic> edge = new FolksonomyEdge<>(author, topic);
		if (sentiment != null) {
			authorSentiments_.put(edge, sentiment);
		} else {
			authorSentiments_.remove(edge);
		}
	}


	private void calculateAuthorSentiments_(TAuthor author) {
		// TODO: We should look into smarter ways of caching which topics apply to a given user
		// (and furthermore, which ones are dirty) so we can avoid checking all topics here.
		for (TTopic topic : getTopics()) {
			calculateAuthorSentiment_(author, topic);
		}
	}


	private void ensureAuthorClean_(TAuthor author) {
		if (dirtyAuthors_.contains(author)) {
			calculateAuthorSentiments_(author);
			dirtyAuthors_.remove(author);
		}
	}

	private void calculateAuthorSentiments_() {
		for (TAuthor author : dirtyAuthors_) {
			calculateAuthorSentiments_(author);
		}
		dirtyAuthors_.clear();
	}
}
