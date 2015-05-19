package fortytwo.vm.constructions;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.field.Field;
import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralArray;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralNumber;

public class FunctionArrayAccess implements Function42 {
	@Override
	public LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments) {
		((LiteralArray) arguments.get(1)).set(
				((LiteralNumber) arguments.get(0)).contents.intValue(),
				arguments.get(1));
	}
	@Override
	public ConcreteType outputType(StaticEnvironment env) {
		for (Field f : from.fields) {
			if (f.name.equals(field)) return f.type;
		}
		throw new RuntimeException(/**/);
	}
	@Override
	public FunctionSignature signature(StaticEnvironment env) {
		return FunctionSignature.getInstance(FunctionName.getInstance(Arrays
				.asList(new FunctionToken("the"), new FunctionToken(
						field.name), new FunctionToken("of"),
						FunctionArgument.INSTANCE)), Arrays
				.asList(from.type), outputType(env));
	}
}
