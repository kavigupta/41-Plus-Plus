package fortytwo.library.standard;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.GenericArrayType;
import fortytwo.language.type.GenericType;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class FunctionArrayLength extends Function42 {
	public static final FunctionArrayLength INSTANCE = new FunctionArrayLength();
	public static final TypeVariable CONTENTS = new TypeVariable(
			VariableIdentifier.getInstance("_length"));
	private FunctionArrayLength() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		return new LiteralNumber(BigDecimal.valueOf(((LiteralArray) arguments
				.get(0)).length()));
	}
	@Override
	public GenericType outputType() {
		return PrimitiveType.NUMBER;
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature
				.getInstance(FunctionName.getInstance("the", "_length",
						"of", ""), Arrays.asList(new GenericArrayType(
						CONTENTS)), outputType());
	}
}
