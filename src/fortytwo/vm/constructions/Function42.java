package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.language.identifier.FunctionSignature;
import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.environment.StaticEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public interface Function42 {
	public LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments);
	public ConcreteType outputType(StaticEnvironment env);
	public FunctionSignature signature(StaticEnvironment env);
}
