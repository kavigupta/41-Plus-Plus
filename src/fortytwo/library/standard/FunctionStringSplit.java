package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralString;

public class FunctionStringSplit extends Function42 {
	public static final FunctionStringSplit INSTANCE = new FunctionStringSplit();
	private FunctionStringSplit() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		Token token = ((LiteralString) arguments.get(0)).contents;
		LiteralArray array = new LiteralArray(PrimitiveType.STRING,
				token.token.length(), Context.synthetic());
		for (int i = 0; i < token.token.length(); i++) {
			array.set(i + 1, new LiteralString(token.subToken(i, i + 1)),
					Context.exprSum(arguments));
		}
		return array;
	}
	@Override
	public ArrayType outputType() {
		return new ArrayType(PrimitiveType.STRING);
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_STRING_SPLIT,
				Arrays.asList(PrimitiveType.STRING), outputType());
	}
}
