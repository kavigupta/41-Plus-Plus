package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.PrimitiveType;
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
		String a = ((LiteralString) arguments.get(0)).contents;
		String b = ((LiteralString) arguments.get(1)).contents;
		return new LiteralString(a + b);
	}
	@Override
	public PrimitiveType outputType() {
		return PrimitiveType.STRING;
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_STRING_APPEND,
				Arrays.asList(PrimitiveType.STRING, PrimitiveType.STRING),
				outputType());
	}
}
