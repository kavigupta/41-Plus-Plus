package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralString;

public class FunctionStringAppend extends Function42 {
	public static final FunctionStringAppend INSTANCE = new FunctionStringAppend();
	private FunctionStringAppend() {}
	@Override
	protected LiteralString apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final LiteralToken a = ((LiteralString) arguments.get(0)).contents;
		final LiteralToken b = ((LiteralString) arguments.get(1)).contents;
		return new LiteralString(LiteralToken.append(a, b));
	}
	@Override
	public PrimitiveType outputType() {
		return new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC);
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_STRING_APPEND,
				Arrays.asList(
						new PrimitiveType(PrimitiveTypeWOC.STRING,
								Context.SYNTHETIC),
						new PrimitiveType(PrimitiveTypeWOC.STRING,
								Context.SYNTHETIC)),
				outputType());
	}
}
