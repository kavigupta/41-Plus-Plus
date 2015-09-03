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
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralString;

public class FunctionStringToArray extends Function42 {
	public static final FunctionStringToArray INSTANCE = new FunctionStringToArray();
	private FunctionStringToArray() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		LiteralToken tokenVal = ((LiteralString) arguments.get(0)).contents;
		LiteralArray larray = new LiteralArray(
				new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC),
				tokenVal.token.length(), Context.SYNTHETIC);
		for (int i = 0; i < tokenVal.token.length(); i++) {
			larray.set(i + 1, new LiteralString(tokenVal.subToken(i, i + 1)),
					Context.sum(arguments));
		}
		return larray;
	}
	@Override
	public PrimitiveType outputType() {
		return new PrimitiveType(PrimitiveTypeWOC.VOID, Context.SYNTHETIC);
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_PRINT, Arrays.asList(
				new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC)),
				outputType());
	}
}
