package folksent.twitfolk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordSentimentMap {

	public Double getWordSentiment(String word) {
		return wordMap_.get(word);
	}

	public void loadFromFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split("\\s+");
			if (fields.length != 2) {
				throw new IOException("Each line must contain a word and sentiment value separated by whitespace");
			}

			wordMap_.put(fields[0], Double.parseDouble(fields[1]));
		}
	}


	public WordSentimentMap() {
		wordMap_ = new HashMap<>();
	}


	private Map<String, Double> wordMap_;
}
