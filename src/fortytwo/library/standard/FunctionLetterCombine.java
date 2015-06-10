package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.compiler.Token42;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ArrayType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWithoutContext;
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
			// pass a synthetic context because we know there will be no
			// error.
			c[i] = ((LiteralString) array.get(i + 1, Context.synthetic())).contents.token
					.charAt(0);
		}
		return new LiteralString(
				new Token42(new String(c), Context.synthetic()));
	}
	@Override
	public PrimitiveType outputType() {
		return new PrimitiveType(PrimitiveTypeWithoutContext.STRING, Context.synthetic());
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_LETTER_COMBINE,
				Arrays.asList(new ArrayType(new PrimitiveType(
						PrimitiveTypeWithoutContext.STRING, Context.synthetic()),
						Context.synthetic())), outputType());
	}
}
