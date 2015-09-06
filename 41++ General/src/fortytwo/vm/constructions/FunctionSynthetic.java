package fortytwo.vm.constructions;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.language.type.FunctionType;
import fortytwo.vm.expressions.LiteralExpression;

public abstract class FunctionSynthetic extends Function42 {
	protected FunctionSynthetic() {
		super(Context.SYNTHETIC);
	}
	@Override
	public String toSourceCode() {
		return "<Built in function " + this.getClass().getSimpleName() + ">";
	}
	@Override
	public ConcreteType resolveType() {
		FunctionSignature sig = signature();
		return new FunctionType(sig.inputTypes, sig.outputType);
	}
	@Override
	public boolean typedEquals(LiteralExpression other) {
		return this == other;
	}
}
