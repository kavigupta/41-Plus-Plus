package fortytwo.vm.constructions;

import java.util.List;

import fortytwo.language.type.ConcreteType;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public interface Function42 {
	public LiteralExpression apply(LocalEnvironment env,
			List<Expression> arguments);
	public ConcreteType outputType();
}
