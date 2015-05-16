package fortytwo.vm.expressions;

import fortytwo.compiler.language.identifier.TypeIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.environment.VariableTypeRoster;
import fortytwo.vm.statements.Statement;

public interface Expression extends Statement {
	LiteralExpression literalValue(LocalEnvironment environment);
	TypeIdentifier resolveType(VariableTypeRoster typeRoster);
}
