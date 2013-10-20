package folksent.model.entity;

public class BaseTopic implements Topic {

	public String getEntityName() {
		return name_;
	}

	@Override
	public boolean equals(Object other) {
		return other != null && other instanceof Topic && ((Topic) other).getEntityName().equals(getEntityName());
	}

	public BaseTopic(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Topic name cannot be null or empty");
		}
		name_ = name;
	}

	private String name_;
}
