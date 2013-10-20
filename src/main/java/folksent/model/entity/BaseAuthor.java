package folksent.model.entity;

public class BaseAuthor implements Author {

	public String getEntityName() {
		return name_;
	}

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof Author && ((Author) other).getEntityName().equals(getEntityName());
	}

	public BaseAuthor(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Author name cannot be null or empty");
		}
		name_ = name;
	}

	private String name_;
}
