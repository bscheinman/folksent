package folksent.model.entity;

public class BaseTopic implements Topic {

	public String getEntityName() {
		return name_;
	}

	public BaseTopic(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Topic name cannot be null or empty");
		}
		name_ = name;
	}

	private String name_;
}
