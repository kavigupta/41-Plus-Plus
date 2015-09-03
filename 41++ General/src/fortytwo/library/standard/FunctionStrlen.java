package fortytwo.library.standard;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import fortytwo.compiler.Context;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.PrimitiveTypeWOC;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;
import fortytwo.vm.expressions.LiteralString;

public class FunctionStrlen extends Function42 {
	public static final FunctionStrlen INSTANCE = new FunctionStrlen();
	private FunctionStrlen() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		return new LiteralNumber(BigDecimal.valueOf(
				((LiteralString) arguments.get(0)).contents.token.length()),
				Context.SYNTHETIC);
	}
	@Override
	public PrimitiveType outputType() {
		return new PrimitiveType(PrimitiveTypeWOC.NUMBER, Context.SYNTHETIC);
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(
				FunctionName.getInstance("the",
						TypeVariable.LENGTH.name.name.token, "of", ""),
				Arrays.asList(new PrimitiveType(PrimitiveTypeWOC.STRING,
						Context.SYNTHETIC)),
				outputType());
	}
}
