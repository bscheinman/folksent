package folksent.model;

import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.Topic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryFolksonomy<TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
		implements Folksonomy<TDocument, TAuthor, TTopic> {

	public Set<TAuthor> getAuthors() {
		return documentsByAuthor_.keySet();
	}


	public Set<TDocument> getDocuments(TAuthor author) {
		return documentsByAuthor_.get(author);
	}


	public Set<TDocument> getDocuments() {
		return topicsByDocument_.keySet();
	}


	public Set<TTopic> getTopics(TDocument document) {
		return topicsByDocument_.get(document);
	}


	public Set<TTopic> getTopics() {
		Set<TTopic> topics = new HashSet<TTopic>();
		for (Set<TTopic> topicSet : topicsByDocument_.values()) {
			for (TTopic topic : topicSet) {
				topics.add(topic);
			}
		}

		return topics;
	}


	public void addDocument(TDocument document) throws FolksonomyException {
		synchronized (this) {
			if (topicsByDocument_.containsKey(document)) {
				throw new FolksonomyException(String.format("Document %s already exists in this folksonomy", document.getEntityName()));
			}

            TAuthor author = documentExtractor_.extractAuthor(document);
            addAuthorDocument_(author, document);

			Set<TTopic> documentTopics = new HashSet<TTopic>();
            topicsByDocument_.put(document, documentTopics);

			for (TTopic topic : documentExtractor_.extractTopics(document)) {
                addDocumentTopic_(document, topic);
			}
		}
	}


	public void removeDocument(TDocument document) {
		synchronized (this) {
			topicsByDocument_.remove(document);
			TAuthor author = documentExtractor_.extractAuthor(document);

			Set<TDocument> authorDocuments = documentsByAuthor_.get(author);
			if (authorDocuments != null) {
				authorDocuments.remove(document);
				if (authorDocuments.isEmpty()) {
					documentsByAuthor_.remove(author);
				}
			}
		}
	}


	public InMemoryFolksonomy(DocumentInformationExtractor<TDocument, TAuthor, TTopic> extractor) {
		documentExtractor_ = extractor;
		topicsByDocument_ = new HashMap<TDocument, Set<TTopic>>();
		documentsByAuthor_ = new HashMap<TAuthor, Set<TDocument>>();
	}


	public InMemoryFolksonomy(InMemoryFolksonomy<TDocument, TAuthor, TTopic> other) {
		this(other.documentExtractor_);
		topicsByDocument_.putAll(other.topicsByDocument_);
		documentsByAuthor_.putAll(other.documentsByAuthor_);
	}

	private DocumentInformationExtractor<TDocument, TAuthor, TTopic> documentExtractor_;
	private Map<TDocument, Set<TTopic>> topicsByDocument_;
	private Map<TAuthor, Set<TDocument>> documentsByAuthor_;


    protected void addAuthorDocument_(TAuthor author, TDocument document) {
        Set<TDocument> authorDocuments = documentsByAuthor_.get(author);
        if (authorDocuments == null) {
            authorDocuments = new HashSet<TDocument>();
            documentsByAuthor_.put(author, authorDocuments);
        }
        authorDocuments.add(document);
    }


    /* TODO: The typical usage pattern will be for all of a document's topics
     * to be added at once, so it might make sense to change the signature of
     * this method in order to avoid looking up the topic set for each insertion.
     */
    protected void addDocumentTopic_(TDocument document, TTopic topic) {
        Set<TTopic> documentTopics = topicsByDocument_.get(document);
        if (documentTopics == null) {
            documentTopics = new HashSet<TTopic>();
            topicsByDocument_.put(document, documentTopics);
        }
        documentTopics.add(topic);
    }
}