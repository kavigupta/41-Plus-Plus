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

public class FunctionLetterCombine extends Function42 {
	public static final FunctionLetterCombine INSTANCE = new FunctionLetterCombine();
	private FunctionLetterCombine() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		LiteralArray array = (LiteralArray) arguments.get(0);
		char[] c = new char[array.length()];
		for (int i = 0; i < array.length(); i++) {
			c[i] = ((LiteralString) array.get(i + 1)).contents.token
					.charAt(0);
		}
		return new LiteralString(new Token(new String(c), Context.minimal()));
	}
	@Override
	public PrimitiveType outputType() {
		return PrimitiveType.STRING;
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_LETTER_COMBINE,
				Arrays.asList(new ArrayType(PrimitiveType.STRING)),
				outputType());
	}
}
