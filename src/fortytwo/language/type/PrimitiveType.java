package fortytwo.language.type;

import fortytwo.compiler.Context;
import fortytwo.vm.expressions.LiteralExpression;

public class PrimitiveType implements ConcreteType {
	public final PrimitiveTypeWithoutContext types;
	private final Context context;
	public PrimitiveType(PrimitiveTypeWithoutContext types, Context context) {
		this.types = types;
		this.context = context;
	}
	@Override
	public Kind kind() {
		return Kind.CONCRETE;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public String toSourceCode() {
		return types.toSourceCode();
	}
	@Override
	public LiteralExpression defaultValue() {
		return types.def;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((types == null) ? 0 : types.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PrimitiveType other = (PrimitiveType) obj;
		if (types != other.types) return false;
		return true;
	}
}
