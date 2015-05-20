package fortytwo.compiler.parsed.statements;

import fortytwo.compiler.parsed.expressions.ParsedExpression;
import fortytwo.language.SourceCode;
import fortytwo.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.StaticEnvironment;
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
	public Statement contextualize(StaticEnvironment environment) {
		return new FieldAssignment(name, environment.structs.typeOf(
				environment.types.typeOf(name), field),
				value.contextualize(environment));
	}
	@Override
	public String toSourceCode() {
		return SourceCode.display(this);
	}
}
