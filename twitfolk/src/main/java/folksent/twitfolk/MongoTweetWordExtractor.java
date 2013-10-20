package folksent.twitfolk;

import java.util.Arrays;
import java.util.List;

public class MongoTweetWordExtractor implements DocumentWordExtractor<MongoDocument> {

	@Override
	public List<String> getWords(MongoDocument document) {
		String text = document.getMongoObject().get("text").toString();
		// TODO: use an actual NLP library for tokenization, but for now splitting on whitespace is fine
		String[] words = text.split("\\s+");
		return Arrays.asList(words);
	}

}
