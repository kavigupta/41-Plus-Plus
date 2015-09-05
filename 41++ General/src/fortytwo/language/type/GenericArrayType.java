package fortytwo.language.type;

import java.util.Optional;

import fortytwo.compiler.Context;
import fortytwo.vm.environment.TypeVariableRoster;

public class GenericArrayType implements GenericType {
	public final GenericType contents;
	private final Context context;
	public GenericArrayType(GenericType contents, Context context) {
		this.contents = contents;
		this.context = context;
	}
	@Override
	public Kind kind() {
		return Kind.CONSTRUCTOR;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public Optional<TypeVariableRoster> match(ConcreteType toMatch) {
		if (!(toMatch instanceof ArrayType)) return Optional.empty();
		return contents.match(((ArrayType) toMatch).contentType);
	}
	@Override
	public ConcreteType resolve(TypeVariableRoster roster) {
		return new ArrayType(contents.resolve(roster), context);
	}
	@Override
	public String toSourceCode() {
		return "(array of " + contents.toSourceCode() + ")";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contents == null ? 0 : contents.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final GenericArrayType other = (GenericArrayType) obj;
		if (contents == null) {
			if (other.contents != null) return false;
		} else if (!contents.equals(other.contents)) return false;
		return true;
	}
}
