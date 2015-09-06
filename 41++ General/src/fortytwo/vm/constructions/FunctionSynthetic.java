package fortytwo.vm.constructions;

import fortytwo.compiler.Context;
import fortytwo.language.type.FunctionType;
import fortytwo.language.type.GenericType;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralFunction;

public final class FunctionSynthetic extends LiteralFunction {
	public static FunctionSynthetic getInstance(FunctionImplementation impl,
			GenericType... types) {
		return new FunctionSynthetic(FunctionType.getInstance(types), impl);
	}
	public FunctionSynthetic(FunctionType type,
			FunctionImplementation implementation) {
		super(Context.SYNTHETIC, type, implementation);
	}
	@Override
	public String toSourceCode() {
		return "<Built in function " + this.getClass().getSimpleName() + ">";
	}
	@Override
	public boolean typedEquals(LiteralExpression other) {
		return this == other;
	}
}
