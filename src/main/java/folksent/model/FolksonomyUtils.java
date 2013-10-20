package folksent.model;

import folksent.model.entity.Author;
import folksent.model.entity.Document;
import folksent.model.entity.FolksonomyEntity;
import folksent.model.entity.Topic;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.AsWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;

public class FolksonomyUtils {

	public static <TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
			Graph<FolksonomyEntity, String> asGraph(Folksonomy<TDocument, TAuthor, TTopic> folksonomy) {

		SimpleGraph<FolksonomyEntity, String> graph = new SimpleGraph<>(new FolksonomyEdgeFactory());

		for (TTopic topic : folksonomy.getTopics()) {
			graph.addVertex(topic);
		}

		for (TAuthor author : folksonomy.getAuthors()) {
			graph.addVertex(author);
			for (TDocument document : folksonomy.getDocuments(author)) {
				graph.addVertex(document);
				graph.addEdge(author, document);

				for (TTopic topic : folksonomy.getTopics(document)) {
					graph.addEdge(document, topic);
				}
			}
		}

		return graph;
	}


	public static <TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
			WeightedGraph<FolksonomyEntity, String> asGraph(AugmentedFolksonomy<TDocument, TAuthor, TTopic> folksonomy) {

		Map<String, Double> weightMap = new HashMap<>();

		EdgeFactory<FolksonomyEntity, String> edgeFactory = new FolksonomyEdgeFactory();
		for (TAuthor author : folksonomy.getAuthors()) {

			Set<TTopic> authorTopics = new HashSet<>();

			for (TDocument document : folksonomy.getDocuments(author)) {
				for (TTopic topic : folksonomy.getTopics(document)) {
					authorTopics.add(topic);
					weightMap.put(edgeFactory.createEdge(document, topic),
							folksonomy.getDocumentSentiment(document, topic));
				}
			}

			for (TTopic topic : authorTopics) {
				weightMap.put(edgeFactory.createEdge(author, topic),
						folksonomy.getAuthorSentiment(author, topic));
			}
		}

		return new AsWeightedGraph<>(asGraph((Folksonomy<TDocument, TAuthor, TTopic>) folksonomy), weightMap);
	}


	private static class FolksonomyEdgeFactory implements EdgeFactory<FolksonomyEntity, String> {

		public char FIELD_SEPARATOR = 0x1e;

		@Override
		public String createEdge(FolksonomyEntity source, FolksonomyEntity target) {
			return String.format("%s%s%s", source.getEntityName(), FIELD_SEPARATOR, target.getEntityName());
		}

	}


	public static class CopyDocumentInformationExtractor<TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
			implements DocumentInformationExtractor<TDocument, TAuthor, TTopic> {

		@Override
		public TAuthor extractAuthor(TDocument document) {
			return original_.getAuthor(document);
		}

		@Override
		public Collection<TTopic> extractTopics(TDocument document) {
			return original_.getTopics(document);
		}

		public CopyDocumentInformationExtractor(Folksonomy<TDocument, TAuthor, TTopic> folksonomy) {
			original_ = folksonomy;
		}

		private Folksonomy<TDocument, TAuthor, TTopic> original_;

	}


	public static class CopySentimentExtractor<TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
			implements SentimentExtractor<TDocument, TAuthor, TTopic> {

		@Override
		public Double getDocumentSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TDocument document, TTopic topic) {
			return original_.getDocumentSentiment(document, topic);
		}

		@Override
		public Double getAuthorSentiment(Folksonomy<TDocument, TAuthor, TTopic> folksonomy, TAuthor author, TTopic topic) {
			return original_.getAuthorSentiment(author, topic);
		}


		public CopySentimentExtractor(AugmentedFolksonomy<TDocument, TAuthor, TTopic> folksonomy) {
			original_ = folksonomy;
		}

		private AugmentedFolksonomy<TDocument, TAuthor, TTopic> original_;
	}


	public static <TDocument extends Document, TAuthor extends Author, TTopic extends Topic>
			void copyFolksonomy(Folksonomy< ? extends TDocument, ? extends TAuthor, ? extends TTopic> from,
			                    Folksonomy<? super TDocument, ? super TAuthor, ? super TTopic> to)
			throws FolksonomyException {

		for (TDocument document : from.getDocuments()) {
			to.addDocument(document);
		}

	}

}
