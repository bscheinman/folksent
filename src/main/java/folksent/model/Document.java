package folksent.model;

public abstract class Document implements FolksonomyBaseModel {

	public String getName() {
		return name_;
	}

	public Document(String name) {
		name_ = name;
	}

	private String name_;
}
