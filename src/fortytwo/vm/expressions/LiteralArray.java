package fortytwo.vm.expressions;

import fortytwo.language.type.ArrayType;
import fortytwo.language.type.ConcreteType;

public class LiteralArray extends LiteralExpression {
	public final ConcreteType contents;
	private final LiteralExpression[] elements;
	public LiteralArray(ConcreteType contents, int length) {
		this.contents = contents;
		this.elements = new LiteralExpression[length];
	}
	@Override
	public ConcreteType resolveType() {
		return new ArrayType(contents);
	}
	public void set(int i, LiteralExpression e) {
		if (e.resolveType().equals(contents))
			throw new RuntimeException(/* LOWPRI-E */);
		elements[i] = e;
	}
}
