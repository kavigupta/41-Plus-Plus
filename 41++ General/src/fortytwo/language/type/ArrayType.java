package fortytwo.language.type;

import fortytwo.compiler.Context;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;

public class ArrayType implements ConcreteType {
	public final ConcreteType contentType;
	private final Context context;
	public ArrayType(ConcreteType contentType, Context context) {
		this.contentType = contentType;
		this.context = context;
	}
	@Override
	public Context context() {
		return context;
	}
	@Override
	public String toSourceCode() {
		return "(array of " + contentType.toSourceCode() + ")";
	}
	@Override
	public LiteralExpression defaultValue() {
		return new LiteralArray(PrimitiveType.SYNTH_VOID, 0, Context.SYNTHETIC);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (contentType == null ? 0 : contentType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final ArrayType other = (ArrayType) obj;
		if (contentType == null) {
			if (other.contentType != null) return false;
		} else if (!contentType.equals(other.contentType)) return false;
		return true;
	}
}
