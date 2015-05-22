package fortytwo.language.type;

import fortytwo.vm.environment.TypeVariableRoster;

public class GenericArrayType implements GenericType {
	public final GenericType contents;
	public GenericArrayType(GenericType contents) {
		this.contents = contents;
	}
	@Override
	public Kind kind() {
		return Kind.CONSTRUCTOR;
	}
	@Override
	public TypeVariableRoster match(ConcreteType toMatch) {
		if (!(toMatch instanceof ArrayType))
			throw new RuntimeException(/* LOWPRI-E */);
		return contents.match(((ArrayType) toMatch).contentType);
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		return new ArrayType(contents.resolve(roster));
	}
	@Override
	public String toSourceCode() {
		return "(array of " + contents.toSourceCode() + ")";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contents == null) ? 0 : contents.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		GenericArrayType other = (GenericArrayType) obj;
		if (contents == null) {
			if (other.contents != null) return false;
		} else if (!contents.equals(other.contents)) return false;
		return true;
	}
}
