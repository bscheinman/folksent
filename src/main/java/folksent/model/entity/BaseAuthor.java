package folksent.model.entity;

public class BaseAuthor implements Author {

	public String getEntityName() {
		return name_;
	}

	public BaseAuthor(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Author name cannot be null or empty");
		}
		name_ = name;
	}

	private String name_;
}
