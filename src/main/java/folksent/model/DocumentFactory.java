package folksent.model;

import java.util.Set;

public abstract class DocumentFactory<T extends Document> {
	
	public final T createDocument(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Document ID cannot be empty");
		}
		if (existingIds_.contains(id)) {
			throw new IllegalArgumentException("Cannot create multiple documents with same ID");
		}
		
		return createDocumentImpl_(id);
	}
	
	
	protected abstract T createDocumentImpl_(String id);
	
	
	private Set<String> existingIds_;
}
