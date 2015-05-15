package fortytwo.vm.expressions;

import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public interface Expression extends Statement {
	LiteralExpression literalValue(LocalEnvironment environment);
}
