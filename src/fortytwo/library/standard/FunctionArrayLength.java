package fortytwo.library.standard;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.*;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class FunctionArrayLength extends Function42 {
	public static final FunctionArrayLength INSTANCE = new FunctionArrayLength();
	private FunctionArrayLength() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		return new LiteralNumber(BigDecimal.valueOf(((LiteralArray) arguments
				.get(0)).length()), Context.synthetic());
	}
	@Override
	public GenericType outputType() {
		return new PrimitiveType(PrimitiveTypeWithoutContext.NUMBER,
				Context.synthetic());
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(FunctionName.getInstance("the",
				TypeVariable.LENGTH.name.name.token, "of", ""), Arrays
				.asList(new GenericArrayType(TypeVariable.LENGTH, Context
						.synthetic())), outputType());
	}
}
