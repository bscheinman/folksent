package folksent.model;

public class FolksonomyGraphNode {
	
	public FolksonomyBaseModel getValue() {
		return value_;
	}
	
	public <T> boolean hasModel(Class<T> clazz) {
		return value_.getClass().isAssignableFrom(clazz);
	}
	
	public <T> T getModel(Class<T> clazz) {
		if (hasModel(clazz)) {
			return (T) value_;
		} else {
			throw new UnsupportedOperationException("This node does not contain a value of type" +
				clazz.getName());
		}
	}
	
	public FolksonomyGraphNode(FolksonomyBaseModel value) {
		value_ = value;
	}
	
	private FolksonomyBaseModel value_;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FolksonomyGraphNode other = (FolksonomyGraphNode) obj;
		if (value_ == null) {
			if (other.value_ != null)
				return false;
		} else if (!value_.equals(other.value_))
			return false;
		return true;
	}
}
