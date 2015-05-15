package fortytwo.compiler.language.statements;

import fortytwo.compiler.language.expressions.ParsedExpression;
import fortytwo.compiler.language.identifier.VariableIdentifier;
import fortytwo.vm.environment.LocalEnvironment;
import fortytwo.vm.statements.Redefinition;
import fortytwo.vm.statements.Statement;

public class ParsedRedefinition implements ParsedAssignment {
	public final VariableIdentifier name;
	public final ParsedExpression expr;
	public ParsedRedefinition(VariableIdentifier name, ParsedExpression expr) {
		this.name = name;
		this.expr = expr;
	}
	@Override
	public Statement contextualize(LocalEnvironment environment) {
		return new Redefinition(name, expr.contextualize(environment));
	}
	@Override
	public SentenceType type() {
		// TODO Auto-generated method stub
		return null;
	}
}
