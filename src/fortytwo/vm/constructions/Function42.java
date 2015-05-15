package fortytwo.vm.constructions;

import java.util.List;

import lib.standard.collections.Pair;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.GlobalEnvironment;
import fortytwo.vm.expressions.Expression;
import fortytwo.vm.expressions.LiteralExpression;

public interface Function42 {
	public LiteralExpression apply(GlobalEnvironment env,
			List<Pair<VariableIdentifier, Expression>> arguments);
}
