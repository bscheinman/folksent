package folksent.model;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Folksonomy<TDocument extends Document, TAuthor extends Author, TTopic extends Topic> {

	public Set<TAuthor> getAuthors() {
		return documentsByAuthor_.keySet();
	}


	public Set<TDocument> getDocumentsByAuthor(TAuthor author) {
		return documentsByAuthor_.get(author);
	}


	public Set<TDocument> getDocuments() {
		return topicsByDocument_.keySet();
	}


	public Set<TTopic> getTopicsByDocument(TDocument document) {
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


	public boolean addDocument(TDocument document) {
		synchronized (this) {
			if (topicsByDocument_.containsKey(document)) {
				return false;
			}

            TAuthor author = documentExtractor_.extractAuthor(document);
            addAuthorDocument_(author, document);

			Set<TTopic> documentTopics = new HashSet<TTopic>();
            topicsByDocument_.put(document, documentTopics);

			for (TTopic topic : documentExtractor_.extractTopics(document)) {
                addDocumentTopic_(document, topic);
			}

			return true;
		}
	}


	public void removeDocument(TDocument document) {
		synchronized (this) {
			topicsByDocument_.remove(document);
			Author author = documentExtractor_.extractAuthor(document);

			Set<TDocument> authorDocuments = documentsByAuthor_.get(author);
			if (authorDocuments != null) {
				authorDocuments.remove(document);
				if (authorDocuments.isEmpty()) {
					documentsByAuthor_.remove(author);
				}
			}
		}
	}


    public SimpleGraph<FolksonomyBaseModel, String> asGraph() {
        SimpleGraph<FolksonomyBaseModel, String> graph =
		        new SimpleGraph<FolksonomyBaseModel, String>(new FolksonomyEdgeFactory());
        populateGraph_(graph);
        return graph;
    }


	public Folksonomy(DocumentInformationExtractor<TDocument, TAuthor, TTopic> extractor) {
		documentExtractor_ = extractor;
		topicsByDocument_ = new HashMap<TDocument, Set<TTopic>>();
		documentsByAuthor_ = new HashMap<TAuthor, Set<TDocument>>();
	}


	public Folksonomy(Folksonomy<TDocument, TAuthor, TTopic> other) {
		this(other.documentExtractor_);
		topicsByDocument_.putAll(other.topicsByDocument_);
		documentsByAuthor_.putAll(other.documentsByAuthor_);
	}

	private DocumentInformationExtractor<TDocument, TAuthor, TTopic> documentExtractor_;
	private Map<TDocument, Set<TTopic>> topicsByDocument_;
	private Map<TAuthor, Set<TDocument>> documentsByAuthor_;


	protected Map<TDocument, Set<TTopic>> getTopicsByDocument_() {
		return topicsByDocument_;
	}

	protected Map<TAuthor, Set<TDocument>> getDocumentsByAuthor_() {
		return documentsByAuthor_;
	}

	protected void populateGraph_(SimpleGraph<FolksonomyBaseModel, String> graph) {
        for (Map.Entry<TAuthor, Set<TDocument>> entry : documentsByAuthor_.entrySet()) {
            TAuthor author = entry.getKey();
            graph.addVertex(author);
            for (TDocument document : entry.getValue()) {
                graph.addVertex(document);
                graph.addEdge(author, document);
            }
        }

        for (Map.Entry<TDocument, Set<TTopic>> entry : topicsByDocument_.entrySet()) {
            TDocument document = entry.getKey();
            for (TTopic topic : entry.getValue()) {
                graph.addVertex(topic);
                graph.addEdge(document, topic);
            }
        }
    }


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


	protected class FolksonomyEdgeFactory implements EdgeFactory<FolksonomyBaseModel, String> {

		@Override
		public String createEdge(FolksonomyBaseModel source, FolksonomyBaseModel target) {
			return source.getName() + 0x1e + target.getName();
		}
	}
}
