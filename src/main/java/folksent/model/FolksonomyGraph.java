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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FolksonomyGraph {

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

		@Override
		public String createEdge(FolksonomyEntity source, FolksonomyEntity target) {
			return source.getEntityName() + 0x1e + target.getEntityName();
		}

	}

}
