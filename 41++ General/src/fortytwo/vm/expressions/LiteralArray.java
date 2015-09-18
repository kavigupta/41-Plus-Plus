package fortytwo.vm.expressions;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.SourceCode;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.errors.RuntimeErrors;

public class LiteralArray extends LiteralExpression {
	public final ConcreteType contents;
	private final LiteralExpression[] elements;
	public LiteralArray(ConcreteType contents, int length, Context context) {
		super(context);
		this.contents = contents;
		this.elements = new LiteralExpression[length];
		for (int i = 0; i < elements.length; i++)
			elements[i] = contents.defaultValue();
	}
	public static LiteralArray getInstance(ConcreteType contents,
			List<? extends LiteralExpression> elements, Context context) {
		final LiteralArray array = new LiteralArray(contents, elements.size(),
				context);
		for (int i = 0; i < elements.size(); i++)
			array.elements[i] = elements.get(i);
		return array;
	}
	@Override
	public ConcreteType resolveType() {
		return new ArrayType(contents, context());
	}
	public void set(int i, LiteralExpression e, Context context) {
		// no type checking here --- should have been done earlier
		try {
			elements[i - 1] = e;
		} catch (final ArrayIndexOutOfBoundsException exc) {
			RuntimeErrors.indexOutOfBoundsException(this, i, context);
		}
	}
	public LiteralExpression get(int i, Context context) {
		try {
			return elements[i - 1];
		} catch (final ArrayIndexOutOfBoundsException exc) {
			RuntimeErrors.indexOutOfBoundsException(this, i, context);
			return null;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contents == null ? 0 : contents.hashCode());
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}
}
