package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.PrimitiveType;
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
		String val = ((LiteralString) arguments.get(0)).contents;
		LiteralArray larray = new LiteralArray(PrimitiveType.STRING,
				val.length());
		for (int i = 0; i < val.length(); i++) {
			larray.set(i + 1,
					new LiteralString(Character.toString(val.charAt(i))));
		}
		return larray;
	}
	@Override
	public PrimitiveType outputType() {
		return PrimitiveType.VOID;
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_PRINT,
				Arrays.asList(PrimitiveType.STRING), outputType());
	}
}