package folksent.model;

public abstract class Topic implements FolksonomyBaseModel {

	public String getName() {
		return name_;
	}

	public Topic(String name) {
		name_ = name;
	}

	private String name_;
}
