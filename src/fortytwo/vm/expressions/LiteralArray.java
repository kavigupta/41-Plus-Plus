package fortytwo.vm.expressions;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.SourceCode;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.ConcreteType;

public class LiteralArray extends LiteralExpression {
	public final ConcreteType contents;
	private final LiteralExpression[] elements;
	public LiteralArray(ConcreteType contents, int length) {
		this.contents = contents;
		this.elements = new LiteralExpression[length];
	}
	public static LiteralArray getInstance(ConcreteType contents,
			List<? extends LiteralExpression> elements) {
		LiteralArray array = new LiteralArray(contents, elements.size());
		for (int i = 0; i < elements.size(); i++) {
			array.elements[i] = elements.get(i);
		}
		return array;
	}
	@Override
	public ConcreteType resolveType() {
		return new ArrayType(contents);
	}
	public void set(int i, LiteralExpression e) {
		if (!e.resolveType().equals(contents))
			throw new RuntimeException(/* LOWPRI-E */);
		try {
			elements[i - 1] = e;
		} catch (ArrayIndexOutOfBoundsException exc) {
			throw new RuntimeException(/* LOWPRI-E */);
		}
	}
	public LiteralExpression get(int i) {
		try {
			return elements[i - 1];
		} catch (ArrayIndexOutOfBoundsException exc) {
			throw new RuntimeException(/* LOWPRI-E */);
		}
	}
	public int length() {
		return elements.length;
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
	@Override
	public boolean typedEquals(LiteralExpression other) {
		return Arrays.deepEquals(this.elements,
				((LiteralArray) other).elements);
	}
}
