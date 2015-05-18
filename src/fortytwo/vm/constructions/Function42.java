package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.expressions.LiteralExpression;

public interface Function42 {
	public LiteralExpression apply(GlobalEnvironment env,
			List<LiteralExpression> arguments);
	public ConcreteType outputType(GlobalEnvironment env);
}
