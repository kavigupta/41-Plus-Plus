package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Statement;

public class ParsedAssignment implements ParsedStatement {
	public final VariableIdentifier name, field;
	public final ParsedExpression parseExpression;
	public ParsedAssignment(VariableIdentifier name, VariableIdentifier field,
			ParsedExpression parseExpression) {
		this.name = name;
		this.field = field;
		this.parseExpression = parseExpression;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SentenceType type() {
		return SentenceType.ASSIGNMENT;
	}
}
