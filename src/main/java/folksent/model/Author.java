package folksent.model;

public abstract class Author implements FolksonomyBaseModel {

	public String getName() { return name_; }

	public Author(String name) {
		name_ = name;
	}

	private final String name_;
}
