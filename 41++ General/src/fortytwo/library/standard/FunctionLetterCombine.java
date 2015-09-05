package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.LiteralToken;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
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
		final LiteralArray array = (LiteralArray) arguments.get(0);
		final char[] c = new char[array.length()];
		for (int i = 0; i < array.length(); i++)
			// pass a synthetic context because we know there will be no
			// error.
			c[i] = ((LiteralString) array.get(i + 1,
					Context.SYNTHETIC)).contents.token.charAt(0);
		return new LiteralString(LiteralToken.synthetic(new String(c)));
	}
	@Override
	public PrimitiveType outputType() {
		return new PrimitiveType(PrimitiveTypeWOC.STRING, Context.SYNTHETIC);
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature
				.getInstance(StdLib42.FUNC_LETTER_COMBINE,
						Arrays.asList(new ArrayType(new PrimitiveType(
								PrimitiveTypeWOC.STRING, Context.SYNTHETIC),
						Context.SYNTHETIC)), outputType());
	}
}
