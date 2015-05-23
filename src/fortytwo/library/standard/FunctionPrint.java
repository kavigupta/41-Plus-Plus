package fortytwo.library.standard;

import java.util.Arrays;
import java.util.List;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.language.type.PrimitiveType;
import fortytwo.language.type.TypeVariable;
import fortytwo.vm.constructions.Function42;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.TypeVariableRoster;
import fortytwo.vm.expressions.LiteralExpression;

public class FunctionPrint extends Function42 {
	public static final FunctionPrint INSTANCE = new FunctionPrint();
	private FunctionPrint() {}
	@Override
	protected LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments, TypeVariableRoster roster) {
		env.machine.displayLine(arguments.get(0).toSourceCode());
		return null;
	}
	@Override
	public PrimitiveType outputType() {
		return PrimitiveType.VOID;
	}
	@Override
	public FunctionSignature signature() {
		return FunctionSignature.getInstance(StdLib42.FUNC_PRINT, Arrays
				.asList(new TypeVariable(VariableIdentifier
						.getInstance("_toPrint"))), outputType());
	}
}
