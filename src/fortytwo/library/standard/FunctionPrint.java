package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.identifier.FunctionName;
import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.functioncomponent.FunctionArgument;
import fortytwo.language.identifier.functioncomponent.FunctionToken;
import fortytwo.language.type.PrimitiveType;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;
import fortytwo.vm.expressions.LiteralString;

public class FunctionPrint extends Function42 {
	public static final FunctionPrint INSTANCE = new FunctionPrint();
	private FunctionPrint() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		env.machine.println(((LiteralString) arguments.get(0)).contents);
		return null;
	}
	@Override
	public PrimitiveType outputType() {
		return PrimitiveType.VOID;
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(FunctionName.getInstance(Arrays
				.asList(new FunctionToken("Tell"), new FunctionToken("me"),
						new FunctionToken("what"),
						FunctionArgument.INSTANCE,
						new FunctionToken("is"))), Arrays
				.asList(PrimitiveType.STRING), outputType());
	}
}
