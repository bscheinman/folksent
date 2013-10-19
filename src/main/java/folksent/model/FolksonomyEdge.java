package folksent.model;

import folksent.model.entity.FolksonomyEntity;

public class FolksonomyEdge<TSource extends FolksonomyEntity, TTarget extends FolksonomyEntity> {

	public TSource getSource() {
		return source_;
	}

	public TTarget getTarget() {
		return target_;
	}

	@Override
	public String toString() {
		return source_.getEntityName() + 0x1e + target_.getEntityName();
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
