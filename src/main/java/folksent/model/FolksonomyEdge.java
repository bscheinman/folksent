package folksent.model;

public class FolksonomyEdge<TSource extends FolksonomyBaseModel, TTarget extends FolksonomyBaseModel> {

	public TSource getSource() {
		return source_;
	}

	public TTarget getTarget() {
		return target_;
	}

	@Override
	public String toString() {
		return source_.getName() + 0x1e + target_.getName();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof FolksonomyEdge)) {
			return false;
		}

		FolksonomyEdge edge = (FolksonomyEdge) other;
		return getSource().equals(edge.getSource()) && getTarget().equals(edge.getTarget());
	}

	public FolksonomyEdge(TSource source, TTarget target) {
		source_ = source;
		target_ = target;
	}

	private TSource source_;
	private TTarget target_;

}
