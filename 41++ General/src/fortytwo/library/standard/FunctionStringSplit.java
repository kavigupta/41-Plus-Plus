package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.vm.constructions.FunctionSynthetic;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralString;

public class FunctionStringSplit extends FunctionSynthetic {
	public static final FunctionStringSplit INSTANCE = new FunctionStringSplit();
	private FunctionStringSplit() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		final LiteralToken token = ((LiteralString) arguments.get(0)).contents;
		final LiteralArray array = new LiteralArray(
				new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC),
				token.token.length(), Context.SYNTHETIC);
		for (int i = 0; i < token.token.length(); i++)
			array.set(i + 1, new LiteralString(token.subToken(i, i + 1)),
					Context.sum(arguments));
		return array;
	}
	@Override
	public ArrayType outputType() {
		return new ArrayType(
				new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC),
				Context.SYNTHETIC);
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_STRING_SPLIT,
				Arrays.asList(new PrimitiveType(PrimitiveTypeWOC.STRING,
						Context.SYNTHETIC)),
				outputType());
	}
}
