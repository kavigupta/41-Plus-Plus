package fortytwo.language.type;

public class ArrayType implements ConcreteType {
	public final ConcreteType contentType;
	public ArrayType(ConcreteType contentType) {
		this.contentType = contentType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentType == null) ? 0 : contentType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ArrayType other = (ArrayType) obj;
		if (contentType == null) {
			if (other.contentType != null) return false;
		} else if (!contentType.equals(other.contentType)) return false;
		return true;
	}
}
