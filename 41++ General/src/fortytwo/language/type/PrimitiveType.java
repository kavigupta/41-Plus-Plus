package fortytwo.language.type;

import fortytwo.compiler.Context;
import fortytwo.vm.expressions.LiteralExpression;

public class PrimitiveType implements ConcreteType {
	public final PrimitiveTypeWOC type;
	private final Context context;
	public PrimitiveType(PrimitiveTypeWOC types, Context context) {
		this.type = types;
		this.context = context;
	}
	public static PrimitiveType synthetic(PrimitiveTypeWOC type) {
		return new PrimitiveType(type, Context.SYNTHETIC);
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
		return type.toSourceCode();
	}
	@Override
	public LiteralExpression defaultValue() {
		return type.def;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final PrimitiveType other = (PrimitiveType) obj;
		if (type != other.type) return false;
		return true;
	}
}
