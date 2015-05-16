package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.FieldAssignment;
import fortytwo.vm.statements.Statement;

public class ParsedFieldAssignment extends ParsedAssignment {
	public final VariableIdentifier name, field;
	public final ParsedExpression value;
	public ParsedFieldAssignment(VariableIdentifier name,
			VariableIdentifier field, ParsedExpression parseExpression) {
		this.name = name;
		this.field = field;
		this.value = parseExpression;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		return new FieldAssignment(name, environment.global.structs.typeOf(
				name, field), value.contextualize(environment));
	}
}
